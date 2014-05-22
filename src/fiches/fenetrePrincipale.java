package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import action.actionAtteindre;
import action.actionAuSujetDe;
import action.actionChercherUnMot;
import action.actionCocheMotAnglais;
import action.actionCocheMotFrancais;
import action.actionCorrection;
import action.actionEnregistrerTraduction;
import action.actionImporter;
import action.actionJouer;
import action.actionNouvelleTraduction;
import action.actionPreferences;
import action.actionRestaurer;
import action.actionSauvegarde;
import action.actionStat;
import action.actionSupprSon;
import action.actionSupprTraduction;
import action.actioninitNouLangue;
import action.reinitConnaissance;

import param.parametres;
import persistence.gestionBases;

import utilitaires.AudioFilePlayer;
import utilitaires.constantes;

import metier.elementTraduc;
import metier.paramLangues;
import net.miginfocom.swing.MigLayout;
/*
 * A faire : un timer sur le passage au mot suivant aprés l'affichage de la traduction
 * Mémorisation de la position et du sens
 */

public class fenetrePrincipale extends JFrame implements ActionListener, KeyListener  {

	// Variables globales
	//Classe contenant une traduction
	private elementTraduc etEnCours;
	// Ensemble des id des traductions
	private ArrayList<Integer> liste;
	// GBVersF Donne le sens 0: GB vers F 1: F vers GB
	// No de la traduction en cours pour les deux sens 
	private int noTraducEnCours = -1;
	private int vusseance = 0;
	private int cochesseance = 0;
	private int start = 0;
	// Eléments d'interface
	private JLabel labelBas;
	private JTextArea editF;
	private JTextArea editGB;
	private JCheckBox editCheckGBOk;
	private JCheckBox editCheckFOk;
	private JButton boutonJouer;
	private JButton boutonSuivant;
	private JButton boutonPrecedent;
	private JButton boutonAffiTraduc;
	private JMenuItem menuEnreg;
	private JButton boutonEnreg;
	private JButton boutonSupprSon;
	// Utilitaires
	Timer timer;          // Timer affichant le mot suivant
	   
    class MonSwingWorker extends SwingWorker<Integer, String> {
		protected Integer doInBackground() throws Exception {
	        final AudioFilePlayer player = new AudioFilePlayer ();
	    	player.play(constantes.getRepMP3() + etEnCours.getFichiermp3());
			return 0;
		}
    }
	 public fenetrePrincipale() {
		 noTraducEnCours = parametres.getInstance().getPositionTraduction();
		 // On mémorise pour pouvoir recommencer la séance
		 start = noTraducEnCours;
		 chargeLibelleLangues();
		 creeInterface();
		 ajouteIcone();
		 //gestion = new gestionBases();
		 if (chargementListeID()) {
			 if (liste.size() > 0) {
				 afficheSuivant();
			 } else {
				 JOptionPane.showMessageDialog(this,
						 "Pas de traduction disponible ", constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			 }
			 setFocusable(true);
		 }
	}
	/*
	 * On charge dans la table des paramètres les libellés des langues 1 et 2
	 * Ces libellés sont toujours là car ils sont obligatoires dans le module
	 * (à écrire) d'initialisation d'un nouveau module de langue
	 */
	private void chargeLibelleLangues() {
		 try {
			gestionBases.getInstance().litLibelleLangues();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors du chargement des libellés des langues :" + 
					e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	private void ajouteIcone() {
		 Image icone = Toolkit.getDefaultToolkit().getImage("images/address-book-new-2.png");
		 this.setIconImage(icone);
	 }
	/**
	 * On charge le type de tri pour présenter les données
	 * Les types de tri sont listés dans le tableau tyepTri 
	 * On peut trier les mots :
	 * "Date de création (Du plus ancien au plus récent)"
	 * "Langue étrangère"
	 * "Langue maternelle"
	 * "Au hasard"
	 * "Date de création (Du plus récent au plus ancien)"
	 */
	public Boolean chargementListeID() {
		try {
			liste = chargeListeID(parametres.getInstance().getTypeTri());
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
							"Erreur lors du chargement de la liste des traductions :" + 
							e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	/**
	 * On va charger une traduction dans la table
	 * On part du l'identifiant de la traduction (clé primaire)
	 * On renvoie une occurrence de l'objet elementTraduc
	 * @param traduc
	 * @return
	 */
	public elementTraduc loadTraduction(Integer no) {
		elementTraduc traduc;
		try {
			traduc = gestionBases.getInstance().chargeUneTraduc( no );
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors du chargement de la traduction no " + Integer.toString(liste.get(0)) + 
							e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return traduc;
	}
	/**
	 * On charge la liste de clé primaires des traductions dans une liste d'entiers
	 * @param tri 
	 * @return : la liste des clés
	 * @throws Exception 
	 * 
	 */
	private ArrayList<Integer> chargeListeID(Integer tri) throws Exception {
		return gestionBases.getInstance().listeMots(tri);
	}
	/**
	 * 
	 */
	private void creeInterface() {
		Font police = new Font(Font.SANS_SERIF , Font.TRUETYPE_FONT, 14);
		this.setFont(police);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.darkGray);

		setTitle(constantes.titreAppli);
		//addMouseListener(this);

		// setSize(1000, 1200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = ajouteMenu();

		setJMenuBar(menuBar);

		add(ajouteBarreOutils(), BorderLayout.PAGE_START);

		getContentPane().add(topPanel);

		JPanel panelTab = new JPanel();
		panelTab.setLayout(new BorderLayout(0, 0));

		JPanel panelPrinc = null;
		try {
			panelPrinc = createPage1();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors de la construction de l'interface graphique :"
							+ e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}

		// Create a tabbed pane

		labelBas = new JLabel();
		labelBas.setText(constantes.titreAppli);
//		labelBas.setBackground( new Color( 133, 156, 221 ) );
		Border myRaisedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color( 133, 156, 221 ),
				new Color( 133, 156, 221 ));
		//Border border = BorderFactory.createRaisedBevelBorder();
		labelBas.setBorder(myRaisedBorder);
		
		topPanel.add(panelPrinc);
		topPanel.add(labelBas, BorderLayout.SOUTH);

		// setContentPane( lePanneau );
		setSize(600,500);
		setLocationRelativeTo(null);
		pack();
		// setUndecorated(true);
		// getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setWindowsLook();
		setLocationRelativeTo(null);
		//setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setVisible(true);
	}
	public JPanel createPage1() {
		JPanel panel1 = new JPanel();
		MigLayout layout = new MigLayout("", "[] 5 [] 5 []", "[] 5 [] ");
		panel1.setLayout(layout);

		JPanel panelT = new JPanel();
		MigLayout layouT = new MigLayout("", "[] 5 []", "[] 5 []");
		panelT.setLayout(layouT);
		Border myRaisedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color( 133, 156, 221 ),new Color( 133, 156, 221 ));
		panelT.setBorder(myRaisedBorder);
		
		JLabel label5 = new JLabel(paramLangues.getInstance().getLibLangue1());
		label5.setBounds(10, 60, 150, 20);
		panelT.add(label5);

		JLabel label6 = new JLabel(paramLangues.getInstance().getLibLangue2());
		label6.setBounds(10, 60, 150, 20);
		panelT.add(label6, "wrap");

		editGB = new JTextArea();
		//editGB.setBounds(10, 80, 150, 20);
		editGB.addKeyListener(this);
		JScrollPane editGBScrollComm = new JScrollPane(editGB);
		editGBScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editGBScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editGBScrollComm.setPreferredSize(new Dimension(400, 300));
//		editId.setFont(police);
		//editGB.setPreferredSize(new Dimension(60, 25));
		//editGB.setEnabled(false);
		panelT.add(editGBScrollComm);

		editF = new JTextArea();
		//editF.setBounds(10, 80, 150, 20);
		editF.addKeyListener(this);
		JScrollPane editFScrollComm = new JScrollPane(editF);
		editFScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editFScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editFScrollComm.setPreferredSize(new Dimension(400, 300));
//		editSerie.setFont(police);
		//editF.setPreferredSize(new Dimension(300, 25));
		panelT.add(editFScrollComm, "wrap");

		panel1.add(panelT, "wrap,span");
		
		editCheckGBOk = new JCheckBox("Ce mot " + paramLangues.getInstance().getLibLangue1().toLowerCase()  + " est désormais connu");
		//editCheckGBOk.setBounds(10, 80, 250, 20);
		//editCheckGBOk.setPreferredSize(new Dimension(500, 25));
		editCheckGBOk.addActionListener(new actionCocheMotAnglais(this));
		panelT.add(editCheckGBOk);

		editCheckFOk = new JCheckBox("Ce mot " + paramLangues.getInstance().getLibLangue2().toLowerCase()  + " est désormais connu");
		//editCheckFOk.setBounds(10, 80, 250, 20);
		//editCheckFOk.setPreferredSize(new Dimension(500, 25));
		editCheckFOk.addActionListener(new actionCocheMotFrancais(this));
		panelT.add(editCheckFOk, "wrap");

		majCheckBox();
		
		boutonJouer = new JButton("Jouer");
		//boutonJouer.setBorder(nul);
		boutonJouer.addActionListener(new actionJouer(this));
		boutonJouer.setMnemonic( KeyEvent.VK_J ) ;
		boutonJouer.setPreferredSize(new Dimension(150,25));
		panel1.add(boutonJouer, "left");

		JButton boutonSelFichierSon = new JButton("...");
		boutonSelFichierSon.setPreferredSize(new Dimension(40,25));
		//boutonSelFichierSon.setBorder(myRaisedBorder);
		boutonSelFichierSon.setToolTipText("Ajouter / Modifier un enregistrement sonore");
		boutonSelFichierSon.addActionListener(this);
		boutonSelFichierSon.setActionCommand("ajoutson");

		boutonSupprSon = new JButton("X");
		boutonSupprSon.setPreferredSize(new Dimension(40,25));
		boutonSupprSon.setToolTipText("Supprimer la référence au son");
		boutonSupprSon.addActionListener(new actionSupprSon(this));
		
		panel1.add(boutonSelFichierSon);
		panel1.add(boutonSupprSon, "wrap,left");

		JPanel panelAction = new JPanel();
    	MigLayout layoutSuivi = new MigLayout();
    	panelAction.setLayout(layoutSuivi);

		boutonPrecedent = new JButton("Précédent");
		boutonPrecedent.setPreferredSize(new Dimension(150,25));
		boutonPrecedent.addActionListener(this);
		boutonPrecedent.setActionCommand("precedent");

		boutonSuivant = new JButton("Suivant");
		boutonSuivant.setPreferredSize(new Dimension(150,25));
		boutonSuivant.addActionListener(this);
		boutonSuivant.setActionCommand("suivant");

		boutonAffiTraduc = new JButton("Afficher la traduction");
		boutonAffiTraduc.setPreferredSize(new Dimension(150,25));
		boutonAffiTraduc.addActionListener(this);
		boutonAffiTraduc.setActionCommand("affitraduc");

		panelAction.add(boutonPrecedent);
		panelAction.add(boutonSuivant);
		panelAction.add(boutonAffiTraduc);
		
		panel1.add(panelAction, "span,center,gapbottom 5");
		return panel1;
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
	/*
	 * 
	 */
	private JMenuBar ajouteMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFichier = new JMenu("Fichier");
		menuFichier.setMnemonic('F');
		JMenu menuEdition = new JMenu("Edition");
		menuEdition.setMnemonic('E');
		JMenu menuOutils = new JMenu("Outils");
		menuOutils.setMnemonic('O');
		JMenu menuAide = new JMenu("Aide");
		menuAide.setMnemonic('A');

		JMenuItem ajoutTraduc = new JMenuItem("Ajouter une traduction");
		ajoutTraduc.addActionListener(new actionNouvelleTraduction(this));
		ajoutTraduc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				KeyEvent.CTRL_DOWN_MASK));
		// Ajout des menus dans la barre de menus et ajout de mnémoniques !
		ajoutTraduc.setMnemonic('A');

		JMenuItem supprTraduc = new JMenuItem("Supprimer cette traduction");
		supprTraduc.addActionListener(new actionSupprTraduction(this));
		supprTraduc.setMnemonic('S');

		JMenuItem affiStats = new JMenuItem("Afficher les statistiques");
		affiStats.addActionListener(new actionStat(this));
		affiStats.setMnemonic('s');

		JMenuItem chouteAgain = new JMenuItem("Recommncez cette série");
		chouteAgain.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						 noTraducEnCours = start;
						 afficheSuivant();
					}
				}
				);
		chouteAgain.setMnemonic('R');

		JMenuItem interrogation = new JMenuItem("Débuter une interrogation");
		interrogation.addActionListener(new actionInterrogation(this));
		interrogation.setMnemonic('i');

		menuEnreg = new JMenuItem("Enregistrer");
		menuEnreg.addActionListener(new actionEnregistrerTraduction(this));
		menuEnreg.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK));
		menuEnreg.setMnemonic('E');

		JMenuItem fermer = new JMenuItem("Quitter");
		fermer.addActionListener(this);
		fermer.setActionCommand("quitter");
		fermer.setMnemonic('Q');
		fermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_DOWN_MASK));

		JMenuItem chercher = new JMenuItem("Rechercher un mot");
		chercher.setMnemonic('R');
		chercher.setAccelerator(KeyStroke.getKeyStroke("F3"));
		chercher.addActionListener(new actionChercherUnMot(this));

		JMenuItem atteindre = new JMenuItem("Atteindre");
		atteindre.setMnemonic('A');
		atteindre.addActionListener(new actionAtteindre(this));

		JMenuItem preferences = new JMenuItem("Préférences");
		preferences.addActionListener(new actionPreferences(this));
		preferences.setMnemonic('P');

		JMenuItem ausujetde = new JMenuItem("Au sujet de");
		ausujetde.addActionListener(new actionAuSujetDe(this)	);
		ausujetde.setMnemonic('A');

		JMenuItem importer = new JMenuItem("Importer des données");
		importer.addActionListener(new actionImporter(this));
		importer.setMnemonic('I');
		importer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				KeyEvent.CTRL_DOWN_MASK));

		JMenuItem sauvegarde = new JMenuItem("Réaliser une sauvegarde");
		sauvegarde.addActionListener(new actionSauvegarde(this));
		sauvegarde.setMnemonic('S');

		JMenuItem restauration = new JMenuItem("Restaurer les données");
		restauration.addActionListener(new actionRestaurer(this));
		restauration.setMnemonic('R');

		JMenuItem initNouLangue = new JMenuItem("Initier une nouvelle langue");
		initNouLangue.addActionListener(new actioninitNouLangue(this));
		initNouLangue.setMnemonic('N');

		JMenuItem reinitC = new JMenuItem("Réinitialiser les éléments 'Connaissance'");
		reinitC.addActionListener(new reinitConnaissance(this));

		JMenuItem correction = new JMenuItem("Correction");
		correction.addActionListener(new actionCorrection(this));

		// On initialise nos menus
		menuFichier.add(ajoutTraduc);
		menuFichier.add(supprTraduc);
		menuFichier.addSeparator();
		menuFichier.add(chouteAgain);
		menuFichier.add(interrogation);
		menuFichier.addSeparator();
		menuFichier.add(affiStats);
		menuFichier.addSeparator();
		menuFichier.add(menuEnreg);
		menuFichier.addSeparator();
		menuFichier.add(fermer);
		
		menuEdition.add(chercher);
		menuEdition.add(atteindre);
		menuEdition.addSeparator();
		menuEdition.add(preferences);
		
		menuAide.add(ausujetde);
		
		menuOutils.add(importer);
		menuOutils.addSeparator();
		menuOutils.add(sauvegarde);
		menuOutils.add(restauration);
		menuOutils.addSeparator();
		menuOutils.add(reinitC);
		menuOutils.add(initNouLangue);
		menuOutils.addSeparator();
		menuOutils.add(correction);

		JMenu menu = new JMenu("L&F");
		ButtonGroup bg = new ButtonGroup();
		Map<String, String> map = getLookAndFeelsMap();
		for (String clef : map.keySet()) {
			final String classe = map.get(clef);
			boolean natif = classe.equals(UIManager
					.getSystemLookAndFeelClassName());
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(clef, natif);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						UIManager.setLookAndFeel(classe);
					} catch (Exception e) {
						e.printStackTrace();
					}
					SwingUtilities
							.updateComponentTreeUI(fenetrePrincipale.this);
				}
			});
			bg.add(item);
			menu.add(item);
		}
		menuBar.add(menuFichier);
		menuBar.add(menuEdition);
		menuBar.add(menuOutils);
		menuBar.add(menu);
		menuBar.add(menuAide);
		return menuBar;
	}
	/*
	 * 
	 */
	private JToolBar ajouteBarreOutils() {
		JToolBar barreOutils = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		barreOutils.setFloatable(false);
		//barreOutils.setBorder(myRaisedBorder);
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		barreOutils.setBorder(loweredetched);


		Image icone = Toolkit.getDefaultToolkit().getImage("images/edit_add.png");
		ImageIcon icon = new ImageIcon(icone);
		JButton boutonAjouter = new JButton(icon);
		boutonAjouter.addActionListener(new actionNouvelleTraduction(this));
		//Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		
		ImageIcon iconeEnreg = new ImageIcon("images/ok.png");
		boutonEnreg = new JButton(iconeEnreg);
		boutonEnreg.setToolTipText("Enregistrer les modifications");
		boutonEnreg.addActionListener(new actionEnregistrerTraduction(this));
		//boutonEnreg.setEnabled(false);

		ImageIcon iconeImport = new ImageIcon("images/import.png");
		JButton boutonImport = new JButton(iconeImport);
		boutonImport.setToolTipText("importer les données d'un fichier CVS");
		boutonImport.addActionListener(new actionImporter(this));

		ImageIcon iconePrems = new ImageIcon("images/premier.png");
		JButton boutonPrems = new JButton(iconePrems);
		boutonPrems.setToolTipText("Premier");
		boutonPrems.setActionCommand("premier");
		boutonPrems.addActionListener(this);

		ImageIcon iconeSuivant = new ImageIcon("images/suivant.png");
		JButton boutonSuivant = new JButton(iconeSuivant);
		boutonSuivant.setToolTipText("Suivant");
		boutonSuivant.setActionCommand("suivant");
		boutonSuivant.addActionListener(this);

		ImageIcon iconePred = new ImageIcon("images/pred.png");
		JButton boutonPred = new JButton(iconePred);
		boutonPred.setToolTipText("Précédent");
		boutonPred.setActionCommand("precedent");
		boutonPred.addActionListener(this);

		ImageIcon iconeDer = new ImageIcon("images/dernier.png");
		JButton boutonDer = new JButton(iconeDer);
		boutonDer.setToolTipText("Dernier");
		boutonDer.setActionCommand("dernier");
		boutonDer.addActionListener(this);

		ImageIcon iconePref = new ImageIcon("images/pref.png");
		JButton boutonPref = new JButton(iconePref);
		boutonPref.setToolTipText("Préférences");
		boutonPref.addActionListener(new actionPreferences(this));

		ImageIcon iconeQuitter = new ImageIcon("images/exit.png");
		JButton boutonQuitter = new JButton(iconeQuitter);
		boutonQuitter.setToolTipText("Quitter l'application");
		boutonQuitter.setActionCommand("quitter");
		boutonQuitter.addActionListener(this);

		barreOutils.add(boutonAjouter);
		barreOutils.add(boutonEnreg);
		barreOutils.addSeparator();
		barreOutils.add(boutonPref);
		barreOutils.addSeparator();
		barreOutils.add(boutonImport);
		barreOutils.addSeparator();
		barreOutils.add(boutonPrems);
		barreOutils.add(boutonPred);
		barreOutils.add(boutonSuivant);
		barreOutils.add(boutonDer);
		barreOutils.addSeparator();
		barreOutils.add(boutonQuitter);

		return barreOutils;
	}
	/*
	 * 
	 */
	private static void setWindowsLook() {
		LookAndFeel lf = UIManager.getLookAndFeel();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
	}
	/**
	 * On choisit le fichier sonore correspondant à la traduction
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
			parametres.getInstance().sauveParamRep(choixfichier.getSelectedFile().getParent());
		    if ( choixfichier.getSelectedFile().getParent() + File.separator !=  constantes.getRepMP3() ) { 
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
		    try {
		    	gestionBases.getInstance().majSon(etEnCours);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement dans la base\n :" +
						e.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		    adapteBouton();
		}
	}
	/*
	 * Look et style de l'application en fonction du l'OS
	 */
	public Map<String, String> getLookAndFeelsMap() {
		UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
		Map<String, String> map = new TreeMap<String, String>();
		for (int i = 0; i < info.length; i++) {
			String nomLF = info[i].getName();
			String nomClasse = info[i].getClassName();
			map.put(nomLF, nomClasse);
		}
		return map;
	}
	/*
	 * On met les deux boutons à enabled / deseable
	 * en fonction de la valeur de traducEnCours (Position dans la lecture)
	 * On empeche l'enregistrement tant tout n'est pas affiché
	 */
	private void adapteBouton() {
		boutonSuivant.setEnabled(noTraducEnCours < (liste.size() - 1));
		boutonPrecedent.setEnabled(noTraducEnCours > 0);
	    boutonJouer.setToolTipText( etEnCours.getFichiermp3() );
	    boutonJouer.setEnabled( etEnCours.getFichiermp3().trim().length() > 0);
		boutonSupprSon.setEnabled(etEnCours.getFichiermp3().trim().length() > 0);
	}
	/**********************************************************
	 * les actions effectuées
	 **********************************************************/
	public void actionPerformed(ActionEvent e) {
		/***********************************************************
		 * Afficher le mot suivant
		 ***********************************************************/
		if (e.getActionCommand().equals("suivant")) {
			afficheSuivant();
		}				
		/***********************************************************
		 * Afficher le mot prédédent
		 ***********************************************************/
		if (e.getActionCommand().equals("precedent")) {
			affichePrecedent();
		}				
		/***********************************************************
		 * Afficher le premier mot 
		 ***********************************************************/
		if (e.getActionCommand().equals("premier")) {
			affichePremier();
		}				
		/***********************************************************
		 * Afficher le dernier mot 
		 ***********************************************************/
		if (e.getActionCommand().equals("dernier")) {
			noTraducEnCours = liste.size() - 1;
			etEnCours = loadTraduction( liste.get(noTraducEnCours) );
			soumettreTraduction( etEnCours );
		}				
		/***********************************************************
		 * On quitte l'application
		 ***********************************************************/
		if (e.getActionCommand().equals("quitter")) {
			parametres.getInstance().sauvePosLecture( noTraducEnCours );
			System.exit(0);
		}
		/***********************************************************
		 * Afficher la traduction du mot
		 ***********************************************************/
		if (e.getActionCommand().equals("affitraduc")) {
			affichageTraduc(etEnCours);
			// Création et lancement du timer
			if (parametres.getInstance().getSuivantAuto()) {
				boutonAffiTraduc.setEnabled(false);
			    timer = new Timer();
				timer.scheduleAtFixedRate(new MonAction(), parametres.getInstance().getDelaisSuivant() * 1000, 5000);
			}
		}				
		/***********************************************************
		 * Slectionner le fichier sonore
		 ***********************************************************/
		if (e.getActionCommand().equals("ajoutson")) {
			selectionneFichierSonore();
		}
	}
	/**
	 * 
	 */
	public void affichePremier() {
		noTraducEnCours = 0;
		etEnCours = loadTraduction( liste.get(noTraducEnCours) );
		soumettreTraduction( etEnCours );
	}
	/**
	 * Affichage de la traduction précédente
	 */
	private void affichePrecedent() {
		if (noTraducEnCours > 0) {
			if (parametres.getInstance().getAfficherTousLesMots()) {
				noTraducEnCours--;
				etEnCours = loadTraduction( liste.get(noTraducEnCours) );
				soumettreTraduction( etEnCours );
			} else {
				int ancIndex = noTraducEnCours;
				do {
					noTraducEnCours--;
					if (noTraducEnCours == 0) {
						etEnCours = loadTraduction( liste.get(ancIndex) );
						soumettreTraduction( etEnCours );
						JOptionPane.showConfirmDialog(this, "Début de fichier atteint", constantes.titreAppli, JOptionPane.WARNING_MESSAGE);
					} else {
						etEnCours = loadTraduction( liste.get(noTraducEnCours) );
						soumettreTraduction( etEnCours );
						soumettreTraduction( etEnCours );
//							System.out.println(traducEnCours);
//							System.out.println( liste.size());
//							System.out.println(etEnCours.getConnu(GBVersF));
					}
				} while ((etEnCours.getConnu(parametres.getInstance().getSens()) && (noTraducEnCours > 0) ));
			}
		}
	}
	/**
	 * On incrémente le compteur de traduction
	 * On charge une traduction à partir de la table
	 * Si on veut toutes les voir : on l'affiche
	 * Sinon on regarde si elle est connue
	 * Si non on recommence
	 */
	private void afficheSuivant() {
		System.out.println(noTraducEnCours);
		System.out.println(liste.size());
		if (noTraducEnCours < liste.size()) {
			if (parametres.getInstance().getTypeTri() == 3) {
					noTraducEnCours = (int)Math.random() * liste.size();
					Random rand = new Random();
					int nombreAleatoire = rand.nextInt(liste.size() - 1 + 1) + 1;
					System.out.println(nombreAleatoire);
					noTraducEnCours = nombreAleatoire;
					etEnCours = loadTraduction( liste.get(noTraducEnCours) );
					soumettreTraduction( etEnCours );
			} else {
				if (parametres.getInstance().getAfficherTousLesMots()) {
					noTraducEnCours++;
					etEnCours = loadTraduction( liste.get(noTraducEnCours) );
					soumettreTraduction( etEnCours );
				} else {
					int ancIndex = noTraducEnCours;
					do {
						noTraducEnCours++;
						if (noTraducEnCours == liste.size() - 1) {
							etEnCours = loadTraduction( liste.get(ancIndex) );
							soumettreTraduction( etEnCours );
							JOptionPane.showConfirmDialog(this, "Fin de fichier atteint", constantes.titreAppli, JOptionPane.WARNING_MESSAGE);
						} else {
							System.out.println(liste.get(noTraducEnCours) );
							etEnCours = loadTraduction( liste.get(noTraducEnCours) );
							soumettreTraduction( etEnCours );
						}
					} while ((etEnCours.getConnu(parametres.getInstance().getSens())) && (noTraducEnCours < (liste.size() - 1)));
				}
			}
			vusseance++;
			boutonAffiTraduc.setEnabled(true);
		} else {
			noTraducEnCours = liste.size() - 1;
			etEnCours = loadTraduction( liste.get(noTraducEnCours) );
			soumettreTraduction( etEnCours );
		}
	}
	class MonAction extends TimerTask {
		public void run() {
			afficheSuivant();
			timer.cancel();
		}
	}
	public void keyTyped(KeyEvent e) {}
	/*
	 * On peut faire avancer le programme avec des touches du clavier
	 * Il faudra modifier les paramètres de l'application pour le rendre modifiable
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent event) {
//		 System.out.println("" + event.getKeyChar());
//		 System.out.println("" + event.getExtendedKeyCode());
		 System.out.println(KeyEvent.getKeyText( event.getKeyCode() ));
		 System.out.println(event.getExtendedKeyCode());
		 if (event.getExtendedKeyCode() == 525) {
			 afficheSuivant();
		 }
		 if (event.getExtendedKeyCode() == 524) {
			 affichageTraduc(etEnCours);
		 }
		 if (event.getExtendedKeyCode() == 9) {
			 if (editGB.isFocusOwner() ) 
				 editF.requestFocus();
			 if (editF.isFocusOwner() ) 
				 editGB.requestFocus();
		 }
	}
	/*
	 * On vient de lire un enregistrement dans la base et on l'affiche
	 * On coche les cases pour les options de connaissance
	 * Mais une seule est affichée
	 * Cela permet lors de la mise à jour de la traduction de ne pas perdre l'option 
	 * d'une des deux langues
	 */
	public void soumettreTraduction( elementTraduc et ) {
		editF.setText("");
		editGB.setText("");
		if (parametres.getInstance().getSens()) {
			editGB.setText(et.getAnglais());
			editGB.requestFocus();
		} else {
			editF.setText(et.getFrancais());
			editF.requestFocus();
		}
		editCheckGBOk.setSelected(et.getGBOk());
		editCheckFOk.setSelected(et.getFOk());
		adapteBouton();
		menuEnreg.setEnabled(false);
		boutonEnreg.setEnabled(false);
		affiLigneStatus();
		if (parametres.getInstance().getJoueTDS()) {
			if (et.getFichiermp3().length() > 0) {
				new MonSwingWorker().execute();
			}
		}
	}
	/**
	 * 
	 */
	public void affiLigneStatus() {
		labelBas.setText( " Mot en cours  : " + (noTraducEnCours + 1) + " sur " + (liste.size()) + "  Mot dans la séance : " + vusseance + " Acquis au cours de la séance : " + cochesseance);
	}
	/**
	 * @param et
	 * On affiche la traduction du mot en cours
	 * Du français si on est dans le sens GB -> F
	 * et inversement
	 */
	private void affichageTraduc(elementTraduc et) {
		if (parametres.getInstance().getSens()) {
			editF.setText(et.getFrancais());
			editF.requestFocus();
			editF.setCaretPosition(0);
		} else {
			editGB.setText(et.getAnglais());
			editGB.requestFocus();
			editGB.setCaretPosition(0);
		}
		menuEnreg.setEnabled(true);
		boutonEnreg.setEnabled(true);
	}
	public elementTraduc getEtEnCours() {
		return etEnCours;
	}
	public void keyReleased(KeyEvent e) {	}
	public String getEditF() {
		return editF.getText() ;
	}
	public String getEditGB() {
		return editGB.getText() ;
	}
	public Boolean getEditCheckGBOk() {
		return editCheckGBOk.isSelected() ;
	}
	public Boolean getEditCheckFOk() {
		return editCheckFOk.isSelected() ;
	}
	public ArrayList<Integer> getListe() {
		return liste;
	}
	public void setEtEnCours(elementTraduc etEnCours) {
		this.etEnCours = etEnCours;
	}
	public int getNoTraducEnCours() {
		return noTraducEnCours;
	}
	public void setNoTraducEnCours( int no ) {
		this.noTraducEnCours = no; 
	}
	public int getCochesseance() {
		return cochesseance;
	}
	public void setCochesseance(int cochesseance) {
		this.cochesseance = cochesseance;
	}
}
