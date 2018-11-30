package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.InputEvent;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.FontPolicies;
import com.jgoodies.looks.FontPolicy;
import com.jgoodies.looks.FontSet;
import com.jgoodies.looks.FontSets;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

import action.actionAjouteSon;
import action.actionAtteindre;
import action.actionAuSujetDe;
import action.actionChercherUnMot;
import action.actionCocheExpression;
import action.actionCocheMotAnglais;
import action.actionCocheMotFrancais;
import action.actionCorrection;
import action.actionEnregistrerTraduction;
import action.actionExporter;
import action.actionImporter;
import action.actionInterrogation;
import action.actionJouer;
import action.actionNouvelleTraduction;
import action.actionPreferences;
import action.actionQuitter;
import action.actionRestaurer;
import action.actionSauvegarde;
import action.actionStat;
import action.actionSupprSon;
import action.actionSupprTraduction;
import action.actioninitNouLangue;
import action.reinitConnaissance;
import param.parametres;
import utilitaires.MonSwingWorker;
import utilitaires.constantes;
import metier.Seance;
import metier.paramLangues;
import net.miginfocom.swing.MigLayout;
/*
 * A faire : un timer sur le passage au mot suivant aprés l'affichage de la traduction
 * Mémorisation de la position et du sens
 */

public class fenetrePrincipale extends JFrame implements ActionListener, KeyListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Eléments d'interface
	private JLabel labelBas;
	private JTextArea editF;
	private JTextArea editGB;
	private JCheckBox editCheckGBOk;
	private JCheckBox editCheckFOk;
	private JCheckBox editCheckExpression;
	private JButton boutonJouer;
	private JButton boutonSuivantBarre;
	private JButton boutonPrecedentBarre;
	private JButton boutonAffTraducBarre;
	private JMenuItem menuEnreg;
	private JButton boutonEnreg;
	private JButton boutonSupprSon;
	// Utilitaires
	Timer timer;          // Timer affichant le mot suivant
	// les variable de la séance de formation qui s'ouvre
	private Seance seance;
	
	/**
	 * 
	 */
	public fenetrePrincipale() {
		 seance = new Seance(this);
		 try {
			 creeInterface();
		 } catch (Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(this,
					 "Erreur de construction de l'interface\n" + e.getLocalizedMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		 }
		 ajouteIcone();

		 if (seance.chargementListeID()) {
			 if (seance.getListe().size() > 0) {
				 try {
					seance.afficheUneTraduction(0);
				} catch (Exception e) {
					e.printStackTrace();
					 JOptionPane.showMessageDialog(this,
							 "Erreur de construction de la lecteure de la table des traductions\n" + e.getLocalizedMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
				}
			 } else {
				 JOptionPane.showMessageDialog(this,
						 "Pas de traduction disponible", constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			 }
			 setFocusable(true);
		 }
	}
	/**
	 * 
	 */
	private void ajouteIcone() {
        Image imageApp = null;
		try {
			imageApp = ImageIO.read(this.getClass().getResource("ressources/iconeappli.png"));
		} catch (IOException e) {
			System.out.println("Erreur de lecture de l'icone : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		 this.setIconImage(imageApp);
	 }
	/**
	 * @throws Exception 
	 * 
	 */
	private void creeInterface() throws Exception {
		setWindowsLook();

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
		Border myRaisedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED , new Color( 133, 156, 221 ),
				new Color( 133, 156, 221 ));
		labelBas.setBorder(myRaisedBorder);
		
		topPanel.add(panelPrinc);
		topPanel.add(labelBas, BorderLayout.SOUTH);

		setSize(600,500);
		setLocationRelativeTo(null);
		pack();
		// getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public JPanel createPage1() {
		JPanel panel1 = new JPanel();
		MigLayout layout = new MigLayout("", "[] 5 [] 5 []", "[] 5 [] ");
		panel1.setLayout(layout);

		JPanel panelT = new JPanel();
		MigLayout layouT = new MigLayout("", "[] 5 []", "[] 5 []");
		panelT.setLayout(layouT);
		//panelT.setBorder(myRaisedBorder);
		
		JLabel label5 = new JLabel(paramLangues.getInstance().getLibLangue1());
		label5.setBounds(10, 60, 150, 20);
		panelT.add(label5);

		JLabel label6 = new JLabel(paramLangues.getInstance().getLibLangue2());
		label6.setBounds(10, 60, 150, 20);
		panelT.add(label6, "wrap");

		editGB = new JTextArea();
		editGB.addKeyListener(this);
		JScrollPane editGBScrollComm = new JScrollPane(editGB);
		editGBScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editGBScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editGBScrollComm.setPreferredSize(new Dimension(400, 300));
		//editGB.setEnabled(false);
		panelT.add(editGBScrollComm);

		editF = new JTextArea();
		editF.addKeyListener(this);
		JScrollPane editFScrollComm = new JScrollPane(editF);
		editFScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editFScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editFScrollComm.setPreferredSize(new Dimension(400, 300));
		//editF.setPreferredSize(new Dimension(300, 25));
		panelT.add(editFScrollComm, "wrap");

		panel1.add(panelT, "wrap,span");
		
		editCheckGBOk = new JCheckBox("Ce mot " + paramLangues.getInstance().getLibLangue1().toLowerCase()  + " est désormais connu");
		editCheckGBOk.setMnemonic( 'c' ) ;
		// Permet que le C avec underscore soit le second et pas la première occurrence
		editCheckGBOk.setDisplayedMnemonicIndex(29);
		editCheckGBOk.addActionListener(new actionCocheMotAnglais(this, seance));
		panelT.add(editCheckGBOk);

		editCheckFOk = new JCheckBox("Ce mot " + paramLangues.getInstance().getLibLangue2().toLowerCase()  + " est désormais connu");
		//editCheckFOk.setPreferredSize(new Dimension(500, 25));
		editCheckFOk.addActionListener(new actionCocheMotFrancais(this, seance));
		panelT.add(editCheckFOk, "wrap");

		editCheckExpression = new JCheckBox("Expression");
		editCheckExpression.addActionListener(new actionCocheExpression(this, seance));
		panelT.add(editCheckExpression, "wrap");
		
		majCheckBox();
		
		boutonJouer = new JButton("Jouer");

		boutonJouer.addActionListener(new actionJouer(seance));
		boutonJouer.setMnemonic( KeyEvent.VK_J ) ;
		boutonJouer.setPreferredSize(new Dimension(150,25));
		panel1.add(boutonJouer, "left");

		JButton boutonSelFichierSon = new JButton("...");
		boutonSelFichierSon.setPreferredSize(new Dimension(40,25));
		//boutonSelFichierSon.setBorder(myRaisedBorder);
		boutonSelFichierSon.setToolTipText("Ajouter / Modifier un enregistrement sonore");
		boutonSelFichierSon.addActionListener(new actionAjouteSon(this, seance));

		boutonSupprSon = new JButton("X");
		boutonSupprSon.setPreferredSize(new Dimension(40,25));
		boutonSupprSon.setToolTipText("Supprimer la référence au son");
		boutonSupprSon.addActionListener(new actionSupprSon(this, seance));
		
		panel1.add(boutonSelFichierSon);
		panel1.add(boutonSupprSon, "wrap,left");

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
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
		menuBar.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY, BorderStyle.SEPARATOR);
		//menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

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

		KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK);
		ajoutTraduc.setAccelerator(ctrlP);
		//ajoutTraduc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		// Ajout des menus dans la barre de menus et ajout de mnémoniques !
		ajoutTraduc.setMnemonic('A');

		JMenuItem supprTraduc = new JMenuItem("Supprimer cette traduction");
		supprTraduc.addActionListener(new actionSupprTraduction(this, seance));
		supprTraduc.setMnemonic('S');

		JMenuItem affiStats = new JMenuItem("Afficher les statistiques");
		affiStats.addActionListener(new actionStat(this));
		affiStats.setMnemonic('s');

		JMenuItem chouteAgain = new JMenuItem("Recommncez cette série");
		chouteAgain.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						 seance.setNoTraducEnCours( seance.getStart() );
						 affiche(0);
					}
				}
				);
		chouteAgain.setMnemonic('R');

		JMenuItem interrogation = new JMenuItem("Débuter une interrogation");
		interrogation.setAccelerator(KeyStroke.getKeyStroke("F11"));
		interrogation.addActionListener(new actionInterrogation(this, seance));
		interrogation.setMnemonic('i');

		menuEnreg = new JMenuItem("Enregistrer");
		menuEnreg.addActionListener(new actionEnregistrerTraduction(this, seance));
		menuEnreg.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK));
		menuEnreg.setMnemonic('E');

		JMenuItem fermer = new JMenuItem("Quitter");
		fermer.addActionListener(new actionQuitter(this, seance));
		fermer.setMnemonic('Q');
		fermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_DOWN_MASK));

		JMenuItem chercher = new JMenuItem("Rechercher un mot");
		chercher.setMnemonic('R');
		chercher.setAccelerator(KeyStroke.getKeyStroke("F3"));
		chercher.addActionListener(new actionChercherUnMot(this));

		JMenuItem atteindre = new JMenuItem("Atteindre");
		atteindre.setMnemonic('A');
		atteindre.addActionListener(new actionAtteindre(this,seance));

		JMenuItem preferences = new JMenuItem("Préférences");
		preferences.addActionListener(new actionPreferences(this, seance));
		preferences.setMnemonic('P');

		JMenuItem ausujetde = new JMenuItem("Au sujet de");
		ausujetde.addActionListener(new actionAuSujetDe(this)	);
		ausujetde.setMnemonic('A');

		JMenuItem importer = new JMenuItem("Importer des données");
		importer.addActionListener(new actionImporter(this));
		importer.setMnemonic('I');
		importer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				KeyEvent.CTRL_DOWN_MASK));

		JMenuItem exporter = new JMenuItem("Exporter des données");
		exporter.addActionListener(new actionExporter(this, seance));
		exporter.setMnemonic('X');
		exporter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
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
		reinitC.addActionListener(new reinitConnaissance(this, seance));

		JMenuItem correction = new JMenuItem("Correction");
		correction.addActionListener(new actionCorrection(this, seance));

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
		menuOutils.add(exporter);
		menuOutils.addSeparator();
		menuOutils.add(sauvegarde);
		menuOutils.add(restauration);
		menuOutils.addSeparator();
		menuOutils.add(reinitC);
		menuOutils.add(initNouLangue);
		menuOutils.addSeparator();
		menuOutils.add(correction);

//		JMenu menu = new JMenu("L&F");
//		ButtonGroup bg = new ButtonGroup();
//		Map<String, String> map = getLookAndFeelsMap();
//		for (String clef : map.keySet()) {
//			final String classe = map.get(clef);
//			boolean natif = classe.equals(UIManager
//					.getSystemLookAndFeelClassName());
//			JRadioButtonMenuItem item = new JRadioButtonMenuItem(clef, natif);
//			item.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent ae) {
//					try {
//						UIManager.setLookAndFeel(classe);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					SwingUtilities
//							.updateComponentTreeUI(fenetrePrincipale.this);
//				}
//			});
//			bg.add(item);
//			menu.add(item);
//		}
		menuBar.add(menuFichier);
		menuBar.add(menuEdition);
		menuBar.add(menuOutils);
//		menuBar.add(menu);
		menuBar.add(menuAide);
		return menuBar;
	}
	/*
	 * 
	 */
	private JToolBar ajouteBarreOutils() throws IOException {
		JToolBar barreOutils = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		barreOutils.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		barreOutils.setFloatable(false);
		//barreOutils.setBorder(myRaisedBorder);
		//Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		//barreOutils.setBorder(loweredetched);


        Image imageEd = ImageIO.read(this.getClass().getResource("ressources/edit_add.png"));
        Icon iconEd = new ImageIcon(imageEd);
		JButton boutonAjouter = new JButton(iconEd);
		boutonAjouter.addActionListener(new actionNouvelleTraduction(this));
		//Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		
        Image image = ImageIO.read(this.getClass().getResource("ressources/ok.png"));
        Icon warningIcon = new ImageIcon(image);
		boutonEnreg = new JButton(warningIcon);
		boutonEnreg.setToolTipText("Enregistrer les modifications");
		boutonEnreg.addActionListener(new actionEnregistrerTraduction(this, seance));

        Image imagePrems = ImageIO.read(this.getClass().getResource("ressources/premier.png"));
        Icon IconPrems = new ImageIcon(imagePrems);
		JButton boutonPrems = new JButton(IconPrems);
		boutonPrems.setToolTipText("Afficher le premier mot");
		boutonPrems.setActionCommand("premier");
		boutonPrems.addActionListener(this);

        Image imageSuiv = ImageIO.read(this.getClass().getResource("ressources/suivant.png"));
        Icon IconSuiv = new ImageIcon(imageSuiv);
        boutonSuivantBarre = new JButton(IconSuiv);
        boutonSuivantBarre.setToolTipText("Afficher le mot suivant");
        boutonSuivantBarre.setActionCommand("suivant");
        boutonSuivantBarre.addActionListener(this);
        
        Image imageAffTraduc = ImageIO.read(this.getClass().getResource("ressources/afftraduc.png"));
        Icon IconAffTraduc = new ImageIcon(imageAffTraduc);
        boutonAffTraducBarre = new JButton(IconAffTraduc);
        boutonAffTraducBarre.setToolTipText("Afficher la traduction");
        boutonAffTraducBarre.setActionCommand("affitraduc");
        boutonAffTraducBarre.addActionListener(this);

        Image imagePred = ImageIO.read(this.getClass().getResource("ressources/pred.png"));
        Icon IconPred = new ImageIcon(imagePred);
        boutonPrecedentBarre = new JButton(IconPred);
        boutonPrecedentBarre.setToolTipText("Afficher le mot précédent");
        boutonPrecedentBarre.setActionCommand("precedent");
        boutonPrecedentBarre.addActionListener(this);

        Image imageDer = ImageIO.read(this.getClass().getResource("ressources/dernier.png"));
        Icon IconDer = new ImageIcon(imageDer);
		JButton boutonDer = new JButton(IconDer);
		boutonDer.setToolTipText("Afficher le dernier mot");
		boutonDer.setActionCommand("dernier");
		boutonDer.addActionListener(this);

        Image imagePref = ImageIO.read(this.getClass().getResource("ressources/pref.png"));
        Icon IconPref = new ImageIcon(imagePref);
		JButton boutonPref = new JButton(IconPref);
		boutonPref.setToolTipText("Préférences");
		boutonPref.addActionListener(new actionPreferences(this, seance));

        Image imageEx = ImageIO.read(this.getClass().getResource("ressources/exit.png"));
        Icon IconEx = new ImageIcon(imageEx);
		JButton boutonQuitter = new JButton(IconEx);
		boutonQuitter.setToolTipText("Quitter l'application");
		boutonQuitter.addActionListener(new actionQuitter(this, seance));

		barreOutils.add(boutonAjouter);
		barreOutils.add(boutonEnreg);
		barreOutils.addSeparator();
		barreOutils.add(boutonPrems);
		barreOutils.add(boutonPrecedentBarre);
		barreOutils.add(boutonAffTraducBarre);
		barreOutils.add(boutonSuivantBarre);
		barreOutils.add(boutonDer);
		barreOutils.addSeparator();
		barreOutils.add(boutonPref);
		barreOutils.add(boutonQuitter);

		return barreOutils;
	}
	/*
	 * 
	 */
	private static void setWindowsLook() {
		PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
		   try {
		      UIManager.setLookAndFeel(new PlasticXPLookAndFeel());// Plastic3DLookAndFeel PlasticXPLookAndFeel
		   } catch (Exception e) {}
		   FontSet fontSet = FontSets.createDefaultFontSet(
				   	new Font("Tahoma", Font.PLAIN, 11),    // control font
				    new Font("Tahoma", Font.PLAIN, 12),    // menu font
				    new Font("Tahoma", Font.BOLD, 11)     // title font
				    );
				FontPolicy fixedPolicy = FontPolicies.createFixedPolicy(fontSet);
				PlasticLookAndFeel.setFontPolicy(fixedPolicy);

	}
	/*
	 * On met les deux boutons à enabled / deseable
	 * en fonction de la valeur de traducEnCours (Position dans la lecture)
	 * On empeche l'enregistrement tant tout n'est pas affiché
	 */
	public void adapteBouton() {
//		boutonSuivantBarre.setEnabled( seance.getNoTraducEnCours() < (seance.getListe().size() - 1));
//		boutonPrecedentBarre.setEnabled( seance.getNoTraducEnCours() > 0);
	    boutonJouer.setToolTipText( seance.getEtEnCours().getFichiermp3() );
	    boutonJouer.setEnabled( seance.getEtEnCours().getFichiermp3().trim().length() > 0);
		boutonSupprSon.setEnabled(seance.getEtEnCours().getFichiermp3().trim().length() > 0);
	}
	/**********************************************************
	 * les actions effectuées
	 **********************************************************/
	public void actionPerformed(ActionEvent e) {
		/***********************************************************
		 * Afficher le mot suivant
		 ***********************************************************/
		if (e.getActionCommand().equals("suivant")) {
			affiche(0);
		}				
		/***********************************************************
		 * Afficher le mot prédédent
		 ***********************************************************/
		if (e.getActionCommand().equals("precedent")) {
			affiche(1);
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
			afficheDernier();
		}				
		/***********************************************************
		 * Afficher la traduction du mot
		 ***********************************************************/
		if (e.getActionCommand().equals("affitraduc")) {
			afficherLaTraduction();
		}				
	}
	/**
	 * 
	 */
	private void afficherLaTraduction() {
		seance.affichageTraduc();
		// Création et lancement du timer
		if (parametres.getInstance().getSuivantAuto()) {
			boutonAffTraducBarre.setEnabled(false);
		    timer = new Timer();
			timer.scheduleAtFixedRate(new MonAction(), parametres.getInstance().getDelaisSuivant() * 1000, 5000);
		}
	}
	/**
	 * 
	 */
	public void affichePremier() {
		try {
			seance.affichePremier();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors du chargement de la traduction no " + Integer.toString( seance.getNoTraducEnCours() ) + 
							e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	public void afficheDernier() {
		try {
			seance.afficheDernier();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors du chargement de la traduction no " + Integer.toString( seance.getNoTraducEnCours() ) + 
							e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	private void affiche(int sens) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
		try {
			seance.afficheUneTraduction( sens );
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors du chargement de la traduction no " + Integer.toString( seance.getNoTraducEnCours() ) + 
							e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	class MonAction extends TimerTask {
		public void run() {
			affiche(0);
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
		 System.out.println("" + event.getExtendedKeyCode());
//		 System.out.println(KeyEvent.getKeyText( event.getKeyCode() ));
//		 System.out.println(event.getExtendedKeyCode());
		 if (event.getExtendedKeyCode() == 525) {
			 affiche(0);
		 }
//		 if (event.getExtendedKeyCode() == 524) {
//			 seance.affichageTraduc();
//		 }
		 if (event.getExtendedKeyCode() == 9) {
			 event.setKeyChar(KeyEvent.CHAR_UNDEFINED);
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
	public void soumettreTraduction() {
		editF.setText("");
		editGB.setText("");
		if (parametres.getInstance().getSens()) {
			editGB.setText(seance.getEtEnCours().getAnglais());
			editGB.requestFocus();
		} else {
			editF.setText(seance.getEtEnCours().getFrancais());
			editF.requestFocus();
		}
		editCheckGBOk.setSelected(seance.getEtEnCours().getGBOk());
		editCheckFOk.setSelected(seance.getEtEnCours().getFOk());
		editCheckExpression.setSelected(seance.getEtEnCours().getExpression());
		adapteBouton();
		menuEnreg.setEnabled(false);
		boutonEnreg.setEnabled(false);
		boutonAffTraducBarre.setEnabled(true);
		if (parametres.getInstance().getJoueTDS()) {
			if (seance.getEtEnCours().getFichiermp3().length() > 0) {
				try {
					new MonSwingWorker(constantes.getRepMP3() + seance.getEtEnCours().getFichiermp3()).execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		seance.incVusseance();
		affiLigneStatus();
	}
	/**
	 * 
	 */
	public void affiLigneStatus() {
		labelBas.setText( seance.getLigneStatus() );
	}
	public void keyReleased(KeyEvent e) {	}
	public JTextArea getEditF() {
		return editF ;
	}
	public JTextArea getEditGB() {
		return editGB ;
	}
	/*
	 * Perme de savoir si la case connu est cochée ou non
	 */
	public Boolean getEditCheckGBOk() {
		return editCheckGBOk.isSelected();
	}
	/*
	 * Perme de savoir si la case connu est cochée ou non
	 */
	public Boolean getEditCheckFOk() {
		return editCheckFOk.isSelected();
	}
	public JButton getBoutonJouer() {
		// TODO Auto-generated method stub
		return boutonJouer;
	}
	/**
	 * @return the menuEnreg
	 */
	public JMenuItem getMenuEnreg() {
		return menuEnreg;
	}
	/**
	 * @return the boutonEnreg
	 */
	public JButton getBoutonEnreg() {
		return boutonEnreg;
	}
	/**
	 * @return the editCheckExpression
	 */
	public Boolean getEditCheckExpression() {
		return editCheckExpression.isSelected();
	}
}
