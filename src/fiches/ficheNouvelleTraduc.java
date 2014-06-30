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
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import fiches.ficheAffiRecherche.MonSwingWorker;

import param.parametres;
import persistence.gestionBases;

import utilitaires.AudioFilePlayer;
import utilitaires.constantes;

import metier.elementTraduc;
import metier.paramLangues;
import net.miginfocom.swing.MigLayout;


public class ficheNouvelleTraduc extends JDialog implements ActionListener {

	private JTextArea editF;
	private JTextArea editGB;
	private JTextField fichierJField;
	private JButton boutonJouer;

    class MonSwingWorker extends SwingWorker<Integer, String> {

		protected Integer doInBackground() throws Exception {
	        final AudioFilePlayer player = new AudioFilePlayer ();
	        System.out.println("On joue : " + fichierJField.getText());
	    	player.play(fichierJField.getText());
			return 0;
		}
    }
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
		
		JLabel label5 = new JLabel(paramLangues.getInstance().getLibLangue1());
		panelT.add(label5);

		JLabel label6 = new JLabel(paramLangues.getInstance().getLibLangue2());
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
		
		fichierJField = new JTextField();
		fichierJField.setPreferredSize(new Dimension(625,25));
		
		JButton boutonSelFichierSon = new JButton("...");
		boutonSelFichierSon.setToolTipText("Ajouter / Modifier un enregistrement sonore");
		boutonSelFichierSon.addActionListener(this);
		boutonSelFichierSon.setActionCommand("ajoutson");

		boutonJouer = new JButton("Jouer");
		boutonJouer.setToolTipText("Joure le fichierJField sonore");
		boutonJouer.addActionListener(this);
		boutonJouer.setEnabled(false);
		boutonJouer.setActionCommand("jouer");

		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[] 5 []", "[] 5 [grow] 5 []");
		panelS.setLayout(layouS);

		panelS.add(new JLabel("Fichier sonore"), "align label");
		panelS.add(fichierJField, "grow");
		panelS.add(boutonSelFichierSon);
		panelS.add(boutonJouer);
		
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
	 * On choisit le fichierJField sonore correspondant à la traduction
	 * On supprimer l'ancien fichierJField s'il existe
	 * On copie le fichierJField dans le dossier des sons
	 * et on enregistre l'information en base
	 */
	private void selectionneFichierSonore() {
		JFileChooser choixfichier = new JFileChooser();
		choixfichier.setDialogTitle("Choix du fichierJField sonore");
		// Empeche de pouvoir sélectionner Tous les fichiers
		choixfichier.setAcceptAllFileFilterUsed(false);
		choixfichier.setCurrentDirectory(new File(parametres.getInstance().loadParamRep()));
		choixfichier.setFileFilter(new filtreFichierSon());
		int returnVal = choixfichier.showOpenDialog(this);
		if ( returnVal == 0) {
			System.out.println(returnVal);
			System.out.println(choixfichier.getSelectedFile().getAbsolutePath());
			parametres.getInstance().sauveParamRep(choixfichier.getSelectedFile().getParent());
			fichierJField.setText( choixfichier.getSelectedFile().getAbsolutePath() );
			boutonJouer.setEnabled(true);
		}
	}	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("jouer")) {
			new MonSwingWorker().execute();
		}
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
			try {
				if (enregsitrementTraduction()) {
					// Copie du fichierJField son dans le dossier des mp3
					if (fichierJField.getText().trim().length() > 0) {
						traiteFichierSon(fichierJField.getText().trim());
					} 
					editGB.setText("");
					editF.setText("");
					fichierJField.setText("");
					boutonJouer.setEnabled(false);
					editGB.requestFocus();
				}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement dans la base\n :" +
						e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		}				
		if (e.getActionCommand().equals("ajoutson")) {
			selectionneFichierSonore();
		}
	}
	/*
	 * On va traiter le fichierJField son
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
			    	JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du fichierJField sonore\n" + ficAnc.getName() + "\n" +
			    									ex.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			    	return;
			    }
		}
	    // Si le fichierJField son n'est pas déja dans le dossier des sont
	    // On le copie dedans
		String repertoire = new File( fichierSon ).getParent() + '/';
		System.out.println(repertoire);
		System.out.println(System.getProperty("user.dir") + '/' + constantes.getRepMP3());
	    if ( repertoire !=  System.getProperty("user.dir") + '/' + constantes.getRepMP3() ) { 
		    // Copie du fichierJField dans le dossier des sons
			File ficIn = new File( fichierSon  );
			//System.out.println(constantes.getRepMP3() + choixfichier.getSelectedFile().getName());
			File ficOut = new File(constantes.getRepMP3() + nomFichier );
			if (! ficIn.renameTo(ficOut)) {
				JOptionPane.showMessageDialog(this, "Erreur lors du déplacement du fichierJField\n" + ficIn.toString() + 
						" vers\n" + ficOut.toString(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
	    }
	}
	/*
	 * Enregistrement des donnés
	 * Si le nom du fichierJField est servi : on ne mémorise que le nom du 
	 * fichierJField et pas le répertoire
	 * Le fichierJField sera ensuite copié dans le dossier des fichiers MP3
	 */
	private boolean enregsitrementTraduction() throws Exception {
		if (gestionBases.getInstance().existeDeja(editGB.getText(), true)) {
			JOptionPane.showMessageDialog(this, "Ce mot existe dèja dans la base", "Nouvel enregistrement", JOptionPane.OK_OPTION);
			return false;
		} else {
			elementTraduc et = new elementTraduc();
			et.setAnglaisSQL(editGB.getText());
			et.setFrancaisSQL(editF.getText());
			et.setFOk(false);
			et.setGBOk(false);
			if (fichierJField.getText().trim().length() > 0) {
				et.setFichiermp3( new File(fichierJField.getText().trim()).getName() );
				boutonJouer.setEnabled(false);
			} else {
				et.setFichiermp3("");
			}
			gestionBases.getInstance().enreg(et);
			return true;
		}
	}
}
