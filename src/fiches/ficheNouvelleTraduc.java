package fiches;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import param.parametres;
import persistence.gestionBases;

import utilitaires.constantes;

import metier.elementTraduc;
import net.miginfocom.swing.MigLayout;


public class ficheNouvelleTraduc extends JDialog implements ActionListener {

	private JTextArea editF;
	private JTextArea editGB;
	private JTextField fichier;

	public ficheNouvelleTraduc() {
		setTitle("Nouvelle traduction");

		JPanel panelSup = new JPanel();
		MigLayout layout = new MigLayout("", "[]", "[] 5 [] 5 []");
    	panelSup.setLayout(layout);
    	
	    setTitle("Nouvelle Traduction");

		JPanel panelT = new JPanel();
		MigLayout layouT = new MigLayout("", "[]", "[] 5 [] 5 []");
		panelT.setLayout(layouT);
		Border myRaisedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color( 133, 156, 221 ),new Color( 133, 156, 221 ));
		panelT.setBorder(myRaisedBorder);
		
		JLabel label5 = new JLabel("Anglais");
		panelT.add(label5);

		JLabel label6 = new JLabel("Français");
		panelT.add(label6, "wrap");

		editGB = new JTextArea(20,40);
		editGB.setBounds(10, 80, 150, 20);
		JScrollPane editGBScrollComm = new JScrollPane(editGB);
		editGBScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editGBScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		editF = new JTextArea(20,40);
		JScrollPane editFScrollComm = new JScrollPane(editF);
		editFScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editFScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		panelT.add(editGBScrollComm);
		panelT.add(editFScrollComm);

		panelSup.add(panelT, "wrap");
		
		fichier = new JTextField();
		fichier.setPreferredSize(new Dimension(625,25));
		
		JButton boutonSelFichierSon = new JButton("...");
		boutonSelFichierSon.setToolTipText("Ajouter / Modifier un enregistrement sonore");
		boutonSelFichierSon.addActionListener(this);
		boutonSelFichierSon.setActionCommand("ajoutson");

		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[] 5 []", "[] 5 [grow] 5 []");
		panelS.setLayout(layouS);

		panelS.add(new JLabel("Fichier sonore"), "align label");
		panelS.add(fichier, "grow");
		panelS.add(boutonSelFichierSon);
		
		JPanel panelB = new JPanel();
		MigLayout layoutB = new MigLayout();
		panelB.setLayout(layoutB);

		JButton boutonEnreg = new JButton("Enregistrer");
		getRootPane().setDefaultButton(boutonEnreg);
		boutonEnreg.setPreferredSize(new Dimension(150,25));
		boutonEnreg.addActionListener(this);
		boutonEnreg.setActionCommand("enreg");

		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });

		panelSup.add(panelS, "wrap");
		
		panelB.add(boutonEnreg, "tag ok");
		panelB.add(boutonQuitter, "tag cancel");
		
		panelSup.add(panelB, "center");

		add(panelSup);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setSize(800,400);
	}
	/**
	 * On choisit le fichier sonore correspondant à la traduction
	 * On supprimer l'ancien fichier s'il existe
	 * On copie le fichier dans le dossier des sons
	 * et on enregistre l'information en base
	 */
	private void selectionneFichierSonore() {
		JFileChooser choixfichier = new JFileChooser();
		choixfichier.setDialogTitle("Choix du fichier sonore");
		// Empeche de pouvoir sélectionner Tous les fichiers
		choixfichier.setAcceptAllFileFilterUsed(false);
		choixfichier.setCurrentDirectory(new File(parametres.getInstance().loadParamRep()));
		choixfichier.setFileFilter(new filtreFichierSon());
		int returnVal = choixfichier.showOpenDialog(this);
		if ( returnVal == 0) {
			System.out.println(returnVal);
			System.out.println(choixfichier.getSelectedFile().getAbsolutePath());
			parametres.getInstance().sauveParamRep(choixfichier.getSelectedFile().getParent());
			fichier.setText( choixfichier.getSelectedFile().getAbsolutePath() );
		}
	}	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("enreg")) {
			if (editGB.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Le mot en langue étrangère n'a pas été saisi", "Nouvelle traduction", JOptionPane.WARNING_MESSAGE);
				editGB.requestFocus();
				return;
			}
			if (editF.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "La traduction n'a pas été saisie", "Nouvelle traduction", JOptionPane.WARNING_MESSAGE);
				editF.requestFocus();
				return;
			}
			if (enregsitrementTraduction()) {
				// Copie du fichier son dans le dossier des mp3
				if (fichier.getText().trim().length() > 0) {
					traiteFichierSon(fichier.getText().trim());
				} 
				editGB.setText("");
				editF.setText("");
				fichier.setText("");
				editGB.requestFocus();
			}
			try {
				if (gestionBases.getInstance().existeDeja(editGB.getText(), true)) {
					JOptionPane.showMessageDialog(this, "Le mot en langue étrangère existe déja", "Nouvelle traduction", JOptionPane.WARNING_MESSAGE);
					editGB.requestFocus();
					return;
				}
				if (gestionBases.getInstance().existeDeja(editGB.getText(), false)) {
					JOptionPane.showMessageDialog(this, "Le mot en langue française existe déja", "Nouvelle traduction", JOptionPane.WARNING_MESSAGE);
					editF.requestFocus();
					return;
				}
			} catch (Exception ex) {
		    	JOptionPane.showMessageDialog(this, "Erreur lors de la recherche de doublon\n" + 
						ex.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		    	return;
			}
		}				
		if (e.getActionCommand().equals("ajoutson")) {
			selectionneFichierSonore();
		}
	}
	/*
	 * On va traiter le fichier son
	 * Si le précédent existe, on le supprime
	 * S'il n'est pas déja présent dans le dossier de destination
	 * des mp3, on l'y déplace
	 */
	private void traiteFichierSon(String fichierSon) {
		String nomFichier = new File( fichierSon ).getName(); 
    	File ficAnc = new File(constantes.getRepMP3() + nomFichier );  
		if (ficAnc.exists()) {
				try {
					ficAnc.delete();
			    } catch( Exception ex ) {
			    	JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du fichier sonore\n" + ficAnc.getName() + "\n" +
			    									ex.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			    	return;
			    }
		}
	    // Si le fichier son n'est pas déja dans le dossier des sont
	    // On le copie dedans
		String repertoire = new File( fichierSon ).getParent() + '/';
		System.out.println(repertoire);
		System.out.println(System.getProperty("user.dir") + '/' + constantes.getRepMP3());
	    if ( repertoire !=  System.getProperty("user.dir") + '/' + constantes.getRepMP3() ) { 
		    // Copie du fichier dans le dossier des sons
			File ficIn = new File( fichierSon  );
			//System.out.println(constantes.getRepMP3() + choixfichier.getSelectedFile().getName());
			File ficOut = new File(constantes.getRepMP3() + nomFichier );
			if (! ficIn.renameTo(ficOut)) {
				JOptionPane.showMessageDialog(this, "Erreur lors du déplacement du fichier\n" + ficIn.toString() + 
						" vers\n" + ficOut.toString(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
	    }
	}
	/*
	 * Enregistrement des donnés
	 * Si le nom du fichier est servi : on ne mémorise que le nom du 
	 * fichier et pas le répertoire
	 * Le fichier sera ensuite copié dans le dossier des fichiers MP3
	 */
	private boolean enregsitrementTraduction() {
		elementTraduc et = new elementTraduc();
		et.setAnglais(editGB.getText());
		et.setFrancais(editF.getText());
		et.setFOk(false);
		et.setGBOk(false);
		if (fichier.getText().trim().length() > 0) {
			et.setFichiermp3( new File(fichier.getText().trim()).getName() );
		} else {
			et.setFichiermp3("");
		}
		try {
			gestionBases.getInstance().enreg(et);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement dans la base\n :" +
					e.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
}
