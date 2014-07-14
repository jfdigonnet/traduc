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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import filtres.filtreFichierSon;

import param.parametres;
import persistence.gestionBases;

import utilitaires.AudioFilePlayer;
import utilitaires.MonSwingWorker;
import utilitaires.constantes;

import metier.elementTraduc;
import metier.paramLangues;
import net.miginfocom.swing.MigLayout;

/*
 * On affiche le résultat d'un recherche
 * On a entre 1 et n traductions à afficher
 * On reçoit une liste des traductions à afficher
 */
public class ficheAffiRecherche extends JDialog implements ActionListener {

	private JTextArea editF;
	private JTextArea editGB;
	private JCheckBox editCheckGBOk;
	private JCheckBox editCheckFOk;
	private ArrayList<Integer> listeCh;
	// No de la traduction en cours pour les deux sens 
	private int traducEnCours = -1;
	private elementTraduc etEnCours;
	private JButton boutonJouer;
	private JButton boutonPrecedent;
	private JButton boutonSuivant;
	private JButton boutonSelFichierSon;
	private JButton boutonSupprSon;
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
		
		JLabel label5 = new JLabel(paramLangues.getInstance().getLibLangue1());
		panelT.add(label5);

		JLabel label6 = new JLabel(paramLangues.getInstance().getLibLangue2());
		panelT.add(label6, "wrap");

		editGB = new JTextArea();
		//editGB.setEditable(false);
		JScrollPane editGBScrollComm = new JScrollPane(editGB);
		editGBScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editGBScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editGBScrollComm.setPreferredSize(new Dimension(400, 300));
		
		editF = new JTextArea();
		//editF.setEditable(false);
		JScrollPane editFScrollComm = new JScrollPane(editF);
		editFScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editFScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editFScrollComm.setPreferredSize(new Dimension(400, 300));

		panelT.add(editGBScrollComm);
		panelT.add(editFScrollComm, "wrap");

		editCheckGBOk = new JCheckBox("Ce mot " + paramLangues.getInstance().getLibLangue1().toLowerCase()  + " est désormais connu");
		editCheckGBOk.addActionListener(this);
		editCheckGBOk.setActionCommand("langue1");
		panelT.add(editCheckGBOk);

		editCheckFOk = new JCheckBox("Ce mot " + paramLangues.getInstance().getLibLangue2().toLowerCase()  + " est désormais connu");
		editCheckFOk.addActionListener(this);
		editCheckFOk.setActionCommand("langue2");
		panelT.add(editCheckFOk, "wrap");

		panelSup.add(panelT, "wrap");

		majCheckBox();
		
		boutonSelFichierSon = new JButton("...");
		boutonSelFichierSon.setToolTipText("Ajouter / Modifier un enregistrement sonore");
		boutonSelFichierSon.addActionListener(this);
		boutonSelFichierSon.setActionCommand("ajoutson");

		boutonSupprSon = new JButton("X");
		boutonSupprSon.setPreferredSize(new Dimension(40,25));
		boutonSupprSon.setToolTipText("Supprimer la référence au son");
		boutonSupprSon.setActionCommand("supprson");
		boutonSupprSon.addActionListener(this);
		
		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[] 5 []", "[] 5 [grow] 5 []");
		panelS.setLayout(layouS);

		boutonJouer = new JButton("Jouer");
		boutonJouer.setActionCommand("jouer");
		boutonJouer.addActionListener(this);
		boutonJouer.setMnemonic( KeyEvent.VK_J ) ;
		boutonJouer.setPreferredSize(new Dimension(150,25));
		boutonJouer.setEnabled(false);

		panelS.add(boutonJouer);
		panelS.add(boutonSelFichierSon);
		panelS.add(boutonSupprSon, "wrap,left");
		
		JPanel panelB = new JPanel();
		MigLayout layoutB = new MigLayout();
		panelB.setLayout(layoutB);

		JButton boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.setPreferredSize(new Dimension(150,25));
		boutonSupprimer.addActionListener(this);
		boutonSupprimer.setActionCommand("supprimer");

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
		boutonEnreg.setActionCommand("enreg");
		boutonEnreg.addActionListener(this);

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
	/**
	 * En fonction du sens de la lecture (GB vers F ou inversement) on 
	 * n'affiche qu'une seule des deux options "Mot connu"
	 */
	public void majCheckBox() {
		if (parametres.getInstance().getSens()) {
			editCheckGBOk.setVisible(true);
			editCheckFOk.setVisible(false);
		} else {
			editCheckGBOk.setVisible(false);
			editCheckFOk.setVisible(true);
		}
	}
	public void actionPerformed(ActionEvent e) {
		/*
		 * A tester
		 */
		if (e.getActionCommand().equals("supprimer")) {
			// On mémorise l'id supprimé pour le supprimer 
			int ancIndex = listeCh.get(traducEnCours);
			// On le supprime de la liste
			listeCh.remove(traducEnCours);
			try {
				// On le supprime de la base de données
				gestionBases.getInstance().supprimeTraduction( ancIndex );
				// On recharge un element de traduction : celui qui occupe la place supprimé
				// Il faudra ajouter un test quand on supprime le dernier
				if (traducEnCours == listeCh.size()) {
					traducEnCours = listeCh.size();
				}
				afficheSuivant();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Une erreur est intervenue lors de la suppression de la traduction " + "\n" + ex.getMessage(),
						constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}

			if (e.getActionCommand().equals("langue1")) {
				etEnCours.setGBOk(editCheckGBOk.isSelected());
				try {
					gestionBases.getInstance().modConnuGB( etEnCours );
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des données (" + paramLangues.getInstance().getLibLangue1() + ") : " + e1.getLocalizedMessage(), 
							"Enregistrement", 
							JOptionPane.ERROR_MESSAGE);			
				}
			}		
		}
		if (e.getActionCommand().equals("langue2")) {
			etEnCours.setFOk(editCheckFOk.isSelected());
			try {
				gestionBases.getInstance().modConnuF( etEnCours );
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des données (" + paramLangues.getInstance().getLibLangue2() + ") : " + e1.getLocalizedMessage(), 
						"Enregistrement", 
						JOptionPane.ERROR_MESSAGE);			
			}
		}				
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

		if (e.getActionCommand().equals("supprson")) {
		    try {
		    	gestionBases.getInstance().supprimeSon(etEnCours);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement dans la base\n :" +
						e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		}				
		if (e.getActionCommand().equals("jouer")) {
	    	try {
				new MonSwingWorker(constantes.getRepMP3() + etEnCours.getFichiermp3().trim()).execute();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}				
		if (e.getActionCommand().equals("enreg")) {
			etEnCours.setAnglaisSQL(this.getEditGB());
			etEnCours.setFrancaisSQL(this.getEditF());
			etEnCours.setGBOk(editCheckGBOk.isSelected());
			etEnCours.setFOk(editCheckFOk.isSelected());
			try {
				gestionBases.getInstance().mod(etEnCours);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des données : " + e1.getLocalizedMessage(), 
						"Enregistrement", 
						JOptionPane.ERROR_MESSAGE);			
			}
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
		editCheckGBOk.setSelected(et.getGBOk());
		editCheckFOk.setSelected(et.getFOk());
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
