package param;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import persistence.gestionBases;
import utilitaires.constantes;
import net.miginfocom.swing.MigLayout;

public class ficheParam extends JDialog implements ActionListener {

	private 	JCheckBox	cb_traducSuivanteAuto;
	private     JTextField  repertoireDonnees;
	private 	JSpinner 	editDelaisSuivant;
	private 	JSpinner 	editTempsAvantInterrogationSuivant;
	private     JCheckBox   cb_afficherTousLesMots;
	private     JCheckBox   cb_enresPosLecture;
	private     JCheckBox   cb_joueTDS;
	private     JCheckBox   cb_memo_interro;
	private     JComboBox<String>	comboSens;
	private     JComboBox<String>	comboTri;
	private     JTextField 			editProxy;

	/***********************************************************************
	 * 
	 ***********************************************************************/
    public ficheParam() {
		JPanel panelSouth = null;
		JPanel panelInternet = null;
		JPanel panelInterrogation = null;
		try {
			panelSouth = panneauGeneral();
			panelInternet = panneauInternet();
			panelInterrogation = panelInterrogation();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors de la construction de l'interface graphique :"
							+ e.getMessage(), "Traduction", JOptionPane.ERROR_MESSAGE);
		}
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Général", panelSouth);
		tabbedPane.addTab("Interrogation", panelInterrogation);
		tabbedPane.addTab("Internet", panelInternet);

		add(tabbedPane, BorderLayout.CENTER);
		
		add(ajouteBoutons(), BorderLayout.SOUTH);
		
		chargeParam();
		majInterface();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
		pack();
		setSize(800,430);
	    setLocationRelativeTo(null);
	}
    /*
     * 
     */
	private JPanel panelInterrogation() {
		JPanel panelSup = new JPanel();
    	MigLayout layoutSup = new MigLayout("", "[] 10 [] 10 []", "[] 10 [] 10 []");
    	panelSup.setLayout(layoutSup);

	    setTitle("Interrogation");

		JLabel texteD = new JLabel("Délai avant la lecture du mot suivant : ");
		panelSup.add(texteD, "align label");
		cb_memo_interro = new JCheckBox("Se souvenir des mots");
		
		Integer value1 = new Integer(1);
		Integer min1 = new Integer(0);
		Integer max1 = new Integer(1000);
		Integer step1 = new Integer(1);
		SpinnerNumberModel model1 = new SpinnerNumberModel(value1, min1, max1, step1);
		editTempsAvantInterrogationSuivant = new JSpinner(model1);
		editTempsAvantInterrogationSuivant.setEditor(new JSpinner.NumberEditor(editTempsAvantInterrogationSuivant, "#"));
		editTempsAvantInterrogationSuivant.setPreferredSize(new Dimension(30, 25));
		panelSup.add(editTempsAvantInterrogationSuivant, "wrap");
		panelSup.add(cb_memo_interro, "wrap");

        JButton boutonOubli = new JButton("Oublier les mots interrogés");
        boutonOubli.setActionCommand("oubli");
        boutonOubli.addActionListener(this);
        boutonOubli.setPreferredSize(new Dimension(180, 25));
        panelSup.add(boutonOubli, "center");
		
		return panelSup;
	}
	/*
	 * 
	 */
	private JPanel panneauInternet() {
		JPanel panelSup = new JPanel();
    	MigLayout layoutSup = new MigLayout("", "[] 10 [] 10 []", "[] 10 [] 10 []");
    	panelSup.setLayout(layoutSup);
    	
	    setTitle("Internet");

	    JLabel texte1 = new JLabel("Proxy :");
	    editProxy = new JTextField(60);
		editProxy.setPreferredSize(new Dimension(30, 25));
		
	    panelSup.add(texte1);
		panelSup.add(editProxy, "wrap");

		return panelSup;
	}
	/***********************************************************************
	 * @return
	 ***********************************************************************/
	private JPanel panneauGeneral() {
		JPanel panelSup = new JPanel();
    	MigLayout layoutSup = new MigLayout("", "[] 10 [] 10 []", "[] 10 [] 10 []");
    	panelSup.setLayout(layoutSup);
    	
	    setTitle("Préférences");
	    
		JPanel panel1 = new JPanel();
    	MigLayout layout1 = new MigLayout("", "", "");
    	panel1.setLayout(layout1);

    	cb_afficherTousLesMots = new JCheckBox ("Afficher tous les mots", false) ;
		cb_afficherTousLesMots.setToolTipText("Afficher tous les mots ou seulement les mots non connus");

		cb_enresPosLecture = new JCheckBox ("Enregistrer la position de la lecture", false) ;

		comboSens =  new JComboBox<String>( constantes.typeSens );
		comboSens.setPreferredSize(new Dimension(150,25));

		JLabel texte1 = new JLabel("Sens des traductions ");

		comboTri =  new JComboBox<String>( constantes.typeTri );
		comboTri.setPreferredSize(new Dimension(150,25));

		JLabel texte2 = new JLabel("Trier les mots dans l'ordre ");

		panel1.add(cb_afficherTousLesMots, "wrap, span 2");
		panel1.add(cb_enresPosLecture, "wrap, span 2");

		panel1.add(texte1, "align label");
		panel1.add(comboSens, "grow, wrap");

		panel1.add(texte2, "align label");
		panel1.add(comboTri, "grow, wrap");
		
		cb_joueTDS = new JCheckBox("Jouer les mots en langue étrangère tout de suite");
		
		panel1.add(cb_joueTDS, "grow, wrap");
		
	    JPanel panel = getPanel("Défilement");
        MigLayout layout = new MigLayout("wrap 2","[] 16 []");
		panel.setLayout(layout);

		cb_traducSuivanteAuto = new JCheckBox ("Afficher automatiquement la traduction suivante", false) ;
		cb_traducSuivanteAuto.setToolTipText("Affichage automatique de la traduction suivante aprés avoir cliqué sur le bouton 'Afficher la traduction'");
		cb_traducSuivanteAuto.addActionListener(this);
		cb_traducSuivanteAuto.setActionCommand("traducsuibanteauto");
		cb_traducSuivanteAuto.setEnabled(true);
	    panel.add(cb_traducSuivanteAuto, "wrap, span 2");

		JLabel texteD = new JLabel("Délai avant affichage de la traduction sauivante : ");
		panel.add(texteD, "align label");
		
		Integer value1 = new Integer(1);
		Integer min1 = new Integer(0);
		Integer max1 = new Integer(1000);
		Integer step1 = new Integer(1);
		SpinnerNumberModel model1 = new SpinnerNumberModel(value1, min1, max1, step1);
		editDelaisSuivant = new JSpinner(model1);
		editDelaisSuivant.setEditor(new JSpinner.NumberEditor(editDelaisSuivant, "#"));
		editDelaisSuivant.setPreferredSize(new Dimension(30, 25));
		editDelaisSuivant.setEnabled(false);
		
		panel.add(editDelaisSuivant);

		JPanel panelRepert = getPanel("Répertoire des données");
		// Par défaut : /home/jfd/workspacediv/GestBD
		//JLabel texte4 = new JLabel("Répertoire des données :");
		//panelRepert.add(texte4);
		repertoireDonnees = new JTextField(60);
		repertoireDonnees.setPreferredSize(new Dimension(30, 25));
		
		panelRepert.add(repertoireDonnees);
        JButton boutonSelFic = new JButton("...");
        boutonSelFic.setActionCommand("selfic");
        boutonSelFic.addActionListener(this);
		panelRepert.add(boutonSelFic);

		/*
		 * Ajout de tous les panels à la fiche
		 */
		panelSup.add(panel1, "wrap, grow");
		panelSup.add(panel, "wrap, grow");
		panelSup.add(panelRepert, "wrap, grow");
		add(panelSup);
		
		return panelSup;
	}
	/*
	 * 
	 */
	private JPanel ajouteBoutons() {
		JPanel panel = new JPanel();
		
		JButton boutonEnreg = new JButton("Enregistrer");
		boutonEnreg.setPreferredSize(new Dimension(150,25));
		boutonEnreg.addActionListener(this);
		boutonEnreg.setActionCommand("enreg");
		panel.add(boutonEnreg);

		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	dispose();
            }        
          });
		panel.add(boutonQuitter);
		return panel;
	}
	private JPanel getPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(title));
		return panel;
	}
	/*
	 * 
	 */
	private Boolean controle() {
		if (cb_traducSuivanteAuto.isSelected() && ((Integer)editDelaisSuivant.getValue() == 0)) {
			JOptionPane.showMessageDialog(this, "Le délai d'attente n'a pas été indiqué" , 
					"Paramètres", 
					JOptionPane.ERROR_MESSAGE);	
			editDelaisSuivant.requestFocus();
			return false;
		}
		if ((Integer)editTempsAvantInterrogationSuivant.getValue() == 0) {
			JOptionPane.showMessageDialog(this, "Le temps avant interrogation suivante ne peut être nul" , 
					"Paramètres", 
					JOptionPane.ERROR_MESSAGE);	
			editTempsAvantInterrogationSuivant.requestFocus();
			return false;
		}
		return true;
	}
	/***********************************************************************
	 * On a chargé les paramètres
	 * On a concu l'interface
	 * On affiche les paramètres
	 ***********************************************************************/
	private void chargeParam() {
		// Sens
		if ( parametres.getInstance().getSens() )
			comboSens.setSelectedIndex(0);
		else
			comboSens.setSelectedIndex(1);
		// Tri
		comboTri.setSelectedIndex(parametres.getInstance().getTypeTri());
		// Position
		cb_enresPosLecture.setSelected(parametres.getInstance().getEnresPosLecteur() );
		//
		cb_afficherTousLesMots.setSelected(parametres.getInstance().getAfficherTousLesMots());
		// 
		repertoireDonnees.setText(parametres.getInstance().getRepertoire_donnees());
		// Tri par mot étranger ou mot français
		//param.setTypeTri(0);
		// Passage à la traduction suivante automatique
		cb_traducSuivanteAuto.setSelected(parametres.getInstance().getSuivantAuto());
		// Délai av	ant de passer au suivant suite à l'affichage d'une traduction
		editDelaisSuivant.setValue(parametres.getInstance().getDelaisSuivant());
		// Proxy
		editProxy.setText(parametres.getInstance().getProxy());
		// Jouer tout de suite
		cb_joueTDS.setSelected( parametres.getInstance().getJoueTDS() );
		// Temps avant interrogation suivante
		editTempsAvantInterrogationSuivant.setValue(parametres.getInstance().getTempsAvantInterrogationSuivante());
		// Mémorisé ou non les mots déja interrogés
		cb_memo_interro.setSelected( parametres.getInstance().getMemoInterro() );
	}
	/**
	 * Enregistrement des paramètres
	 */
	private void enregParam() {
		// Sens
		parametres.getInstance().setSens(comboSens.getSelectedIndex());
		// Tri
		parametres.getInstance().setTypeTri(comboTri.getSelectedIndex());
		// Position lecteur
		parametres.getInstance().setEnresPosLecteur(cb_enresPosLecture.isSelected());
		//
		parametres.getInstance().setAfficherTousLesMots(cb_afficherTousLesMots.isSelected());
		// Répertoire d'enregistrement des données
		parametres.getInstance().setRepertoire_donnees(repertoireDonnees.getText());
		// Passage à la traduction suivante automatique
		parametres.getInstance().setSuivantAuto(cb_traducSuivanteAuto.isSelected());
		// Délai av	ant de passer au suivant suite à l'affichage d'une traduction
		parametres.getInstance().setDelaisSuivant((Integer)editDelaisSuivant.getValue());
		// Proxy
		parametres.getInstance().setProxy(editProxy.getText());
		// Jouer tout de suite
		parametres.getInstance().setJoueTDS(cb_joueTDS.isSelected());
		//
		parametres.getInstance().setTempsAvantInterrogationSuivante((Integer)editTempsAvantInterrogationSuivant.getValue());
		// Mémo interro
		parametres.getInstance().setMemoInterro(cb_memo_interro.isSelected()); 
		// Enregistrement
		parametres.getInstance().saveParam();
	}
	/*
	 * 
	 */
	private void majInterface() {
		editDelaisSuivant.setEnabled(parametres.getInstance().getSuivantAuto());
	}
	/***********************************************************************
	 * Vérification et enregistrement des données
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 ***********************************************************************/
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JButton) {
			JButton source = (JButton)e.getSource() ;
			if (source.getActionCommand().equals("enreg")) {
				if (controle()) {
					enregParam();
				}
			}
			if (source.getActionCommand().equals("traducsuibanteauto")) {
			}
		}
        if (e.getActionCommand().equals("oubli") ) {
        	try {
				gestionBases.getInstance().oubliIterrogation();
				JOptionPane.showMessageDialog(this, "Traitement terminé",
						"Paramètres", 
						JOptionPane.INFORMATION_MESSAGE );	
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Une erreur est intervenue lors de la ré initialisation de cet élément\n" +
						e1.getLocalizedMessage() , 
						"Paramètres", 
						JOptionPane.ERROR_MESSAGE);	
			}
        }
        if (e.getActionCommand().equals("selfic") ) {
            JFileChooser editChoixRepertoire = new JFileChooser();
            editChoixRepertoire.setDialogTitle("Choix du fichier à intégrer");
            editChoixRepertoire.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            // Empeche de pouvoir sélectionner Tous les fichiers
            editChoixRepertoire.setAcceptAllFileFilterUsed(false);
            editChoixRepertoire.setCurrentDirectory(new File(System.getProperty("user.dir")));
            //editChoixRepertoire.setFileFilter(new filtreFichierImport());
            int returnVal = editChoixRepertoire.showOpenDialog(this);
            if ( returnVal == 0) {
                File file = editChoixRepertoire.getSelectedFile();
                repertoireDonnees.setText(file.toString());
            }
        }
		if (e.getSource() instanceof JCheckBox) {
			Object source = (JCheckBox)e.getSource() ;
			//if (JCheckBox == cb_navigateurParDefaut) {
			// ou 
	        if (source == cb_traducSuivanteAuto) { 
	        	cliqueCheckedBoxDelaiAuto();
	        }
		}
	}
	/***********************************************************************
	 * 
	 ***********************************************************************/
	private void cliqueCheckedBoxDelaiAuto() {
		if (cb_traducSuivanteAuto.isSelected()) {
			editDelaisSuivant.setEnabled(true);
		} else {
			editDelaisSuivant.setEnabled(false);
		}
	}
	// Permet de quitter la fiche par la touche ECHAP
	private void onKeyEscape() {
		this.dispose();
	}
	private void configureRootPane(JRootPane rootPane) {
	    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escPressed");
	 
	    rootPane.getActionMap().put(
	        "escPressed",
	        new AbstractAction("escPressed") {
	          public void actionPerformed(ActionEvent actionEvent) {
	            onKeyEscape();
	          }
	        });
	  }
}

