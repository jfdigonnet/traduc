package fiches;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

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
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import action.actionEnregistrerTraduction;
import action.actionJouer;
import action.actionSupprSon;

import param.parametres;
import persistence.gestionBases;

import utilitaires.constantes;

import metier.elementTraduc;
import net.miginfocom.swing.MigLayout;


public class ficheAffiRecherche extends JDialog implements ActionListener {

	private JTextArea editF;
	private JTextArea editGB;
	private ArrayList<Integer> listeCh;
	// No de la traduction en cours pour les deux sens 
	private int traducEnCours = -1;
	private elementTraduc etEnCours;
	private JButton boutonJouer;
	private JButton boutonPrecedent;
	private JButton boutonSuivant;
	// Dernier répertoire utilisé
	private String lastrepert = "";

	public ficheAffiRecherche( ArrayList<Integer> liste) {
		lastrepert = parametres.getInstance().loadParamRep();
		this.listeCh = liste;
		JPanel panelSup = new JPanel();
		MigLayout layout = new MigLayout("", "[]", "[] 5 [] 5 []");
    	panelSup.setLayout(layout);
    	
	    setTitle("Résultat de la recherche");

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
		//editGB.setEditable(false);
		JScrollPane editGBScrollComm = new JScrollPane(editGB);
		//editGBScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//editGBScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		editF = new JTextArea(20,40);
		//editF.setEditable(false);
		JScrollPane editFScrollComm = new JScrollPane(editF);
		//editFScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//editFScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		panelT.add(editGBScrollComm);
		panelT.add(editFScrollComm);

		panelSup.add(panelT, "wrap");
		
		JButton boutonSelFichierSon = new JButton("...");
		boutonSelFichierSon.setToolTipText("Ajouter / Modifier un enregistrement sonore");
		boutonSelFichierSon.addActionListener(this);
		boutonSelFichierSon.setActionCommand("ajoutson");

		JButton boutonSupprSon = new JButton("X");
		boutonSupprSon.setPreferredSize(new Dimension(40,25));
		boutonSupprSon.setToolTipText("Supprimer la référence au son");
		boutonSupprSon.addActionListener(new actionSupprSon(this));
		
		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[] 5 []", "[] 5 [grow] 5 []");
		panelS.setLayout(layouS);

		boutonJouer = new JButton("Jouer");
		boutonJouer.addActionListener(new actionJouer(this));
		boutonJouer.setMnemonic( KeyEvent.VK_J ) ;
		boutonJouer.setPreferredSize(new Dimension(150,25));
		boutonJouer.setEnabled(false);

		panelS.add(boutonJouer);
		panelS.add(boutonSelFichierSon);
		panelS.add(boutonSupprSon, "wrap,left");
		
		JPanel panelB = new JPanel();
		MigLayout layoutB = new MigLayout();
		panelB.setLayout(layoutB);

		boutonPrecedent = new JButton("Précédent");
		boutonPrecedent.setPreferredSize(new Dimension(150,25));
		boutonPrecedent.addActionListener(this);
		boutonPrecedent.setActionCommand("precedent");

		boutonSuivant = new JButton("Suivant");
		boutonSuivant.setPreferredSize(new Dimension(150,25));
		boutonSuivant.addActionListener(this);
		boutonSuivant.setActionCommand("suivant");

		JButton boutonEnreg = new JButton("Enregistrer");
		boutonEnreg.setToolTipText("Enregistrer les modifications");
		boutonEnreg.addActionListener(new actionEnregistrerTraduction(this));

		JButton boutonQuitter = new JButton("Fermer");
		getRootPane().setDefaultButton(boutonQuitter);
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });

		panelSup.add(panelS, "wrap");
		
		panelB.add(boutonPrecedent, "tag ok");
		panelB.add(boutonSuivant, "tag ok");
		panelB.add(boutonEnreg);
		panelB.add(boutonQuitter, "tag cancel");
		
		panelSup.add(panelB, "center");

		add(panelSup);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setSize(800,400);
	    setLocationRelativeTo(null);
	    
	    afficheSuivant();
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("fermer")) {
			this.setVisible(false);
		}				
		if (e.getActionCommand().equals("precedent")) {
			affichePrecedent();
		}				
		if (e.getActionCommand().equals("suivant")) {
			afficheSuivant();
		}				
		if (e.getActionCommand().equals("ajoutson")) {
			selectionneFichierSonore();
		}
	}
	private elementTraduc loadTraduction(Integer no) {
		elementTraduc traduc;
		try {
			traduc = gestionBases.getInstance().chargeUneTraduc( no );
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors du chargement de la traduction no " + Integer.toString(listeCh.get(0)) + 
							e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return traduc;
	}
	private void afficheUneTraduction( elementTraduc et ) {
		editF.setText("");
		editGB.setText("");
		editGB.setText(et.getAnglais());
		editF.setText(et.getFrancais());
		boutonJouer.setToolTipText(etEnCours.getFichiermp3().trim());
		editGB.requestFocus();
		editGB.setCaretPosition(0);
		adapteBouton();
	}
	private void affichePrecedent() {
		if (traducEnCours > 0) {
				traducEnCours--;
				etEnCours = loadTraduction( listeCh.get(traducEnCours) );
				afficheUneTraduction( etEnCours );
		}
	}
	private void afficheSuivant() {
		if (traducEnCours <= listeCh.size() - 1) {
				traducEnCours++;
				etEnCours = loadTraduction( listeCh.get(traducEnCours) );
				afficheUneTraduction( etEnCours );
		}
	}
	private void adapteBouton() {
		boutonSuivant.setEnabled(traducEnCours < (listeCh.size() - 1));
		boutonPrecedent.setEnabled(traducEnCours > 0);
		System.out.println("Fichier son " + etEnCours.getFichiermp3());
		System.out.println(etEnCours.getFichiermp3().trim().length());
		boutonJouer.setEnabled( etEnCours.getFichiermp3().trim().length() > 0);
		setTitle("Résultat de la recherche " + (traducEnCours + 1) + "/" + listeCh.size());
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
		if (lastrepert.length() > 0) {
			choixfichier.setCurrentDirectory(new File(lastrepert));
		} else {
			choixfichier.setCurrentDirectory(new File(System.getProperty("user.dir")));
		}
		choixfichier.setFileFilter(new filtreFichierSon());
		int returnVal = choixfichier.showOpenDialog(this);
		if ( returnVal == 0) {
			parametres.getInstance().sauveParamRep(choixfichier.getSelectedFile().getParent());
			// Ex : /home/jfd/workspacediv/traduc/anglais.csv
		    //System.out.println( choixfichier.getSelectedFile().getAbsolutePath() );
		    // Renvoie le répertoire
		    //System.out.println( choixfichier.getSelectedFile().getParent() );
		    // Renvoie le nom du fichier
		    //System.out.println( choixfichier.getSelectedFile().getName() );
		    // Si le fichier son n'est pas déja dans le dossier des sons
		    // On le copie dedans
		    if ( choixfichier.getSelectedFile().getParent() + File.separator !=  constantes.getRepMP3() ) {
		    	System.out.println(choixfichier.getSelectedFile().getParent());  
		    	System.out.println(constantes.getRepMP3());
			    // Copie du fichier dans le dossier des sons
				File ficIn = new File( choixfichier.getSelectedFile().getAbsolutePath()  );
				//System.out.println(constantes.getRepMP3() + choixfichier.getSelectedFile().getName());
				File ficOut = new File(constantes.getRepMP3() + choixfichier.getSelectedFile().getName());
				if (! ficIn.renameTo(ficOut)) {
					JOptionPane.showMessageDialog(this, "Erreur lors du déplacement du fichier\n" + ficIn.toString() + 
							" vers\n" + ficOut.toString(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
				}
		    }
		    etEnCours.setFichiermp3( choixfichier.getSelectedFile().getName() );
		    System.out.println( choixfichier.getSelectedFile().getName() );
		    try {
		    	gestionBases.getInstance().majSon(etEnCours);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement dans la base\n :" +
						e.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		    boutonJouer.setToolTipText(constantes.getRepMP3() + choixfichier.getSelectedFile().toString());
		    boutonJouer.setEnabled(true);
		}
	}
	public elementTraduc getEtEnCours() {
		return etEnCours;
	}
	public String getEditF() {
		return editF.getText() ;
	}
	public String getEditGB() {
		return editGB.getText() ;
	}
}
