package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import metier.Seance;
import metier.elementTraduc;
import net.miginfocom.swing.MigLayout;

import persistence.gestionBases;
import utilitaires.constantes;

public class ficheCorrection extends JDialog implements ActionListener {

	// Fiche principale
	private Component application;
	private Seance seance;
	//Classe contenant une traduction
	private elementTraduc etEnCours;
    private 	JButton 	okBouton;
    private 	JProgressBar progressBar;
    private     JTextArea suivi ;
    private     Boolean   simulation = true;

    class threadCorrection extends SwingWorker<Integer, String> {

        public threadCorrection() {
            /* On ajoute un écouteur de barre de progression. */
            addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if("progress".equals(evt.getPropertyName())) {
                        progressBar.setValue((Integer) evt.getNewValue());
                    }
                }
            });
        }
		protected Integer doInBackground() {
	    	Integer nbCor = 0;
	    	progressBar.setMaximum( seance.getListe().size() - 1);
	    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			suivi.append("Nombre de traduction : " + seance.getListe().size() + "\n");
			suivi.append("-------------------------------------------\n");
	    	try {
		    	if (seance.getListe().size() > 0) {
		    		for (int i=0; i<(seance.getListe().size()); i++) {
		        			progressBar.setValue(i+1);
		        			System.out.println(i);
		        			try {
		        				etEnCours = gestionBases.getInstance().chargeUneTraduc( seance.getListe().get(seance.getListe().get(i)) );
		        			} catch (Exception e1) {
		        				JOptionPane.showMessageDialog(null, 
		        						"Importer : Une erreur est intervenue lors de la lecture de la table : " + e1.getLocalizedMessage() + "\n" +  
		        						"Indice de lecture : " + seance.getListe().get(seance.getListe().get(i)) + "\n" + 
		        						"Arrêt de la procédure", 
		        						"Correction", 
		        						JOptionPane.WARNING_MESSAGE, 
		        						null);
		        				return 0;
		        			}
		        			String Chaine = etEnCours.getFichiermp3();
		        			if ( Chaine.contains("/")) { 
		        				//suivi.append(i + " : " + Chaine + "\n");
		        				File fic = new File(etEnCours.getFichiermp3());
		        				etEnCours.setFichiermp3(fic.getName());
		        				suivi.append("Mise à jour : Suppression répertoire : " + etEnCours.getFichiermp3() + "\n ");
		        				suivi.setCaretPosition(suivi.getText().length());
		        				try {
		        					if (!simulation)  gestionBases.getInstance().majSon(etEnCours);
			        			} catch (Exception e1) {
			        				JOptionPane.showMessageDialog(null, 
			        						"Importer : Une erreur est intervenue lors de la mise à jour du répertoire : " + e1.getLocalizedMessage() + "\nArrêt de la procédure", 
			        						"Correction", 
			        						JOptionPane.WARNING_MESSAGE, 
			        						null);
			        				return 0;
			        			}
		        				nbCor++;
		        			}
		        			if ( Chaine.contains("null")) { 
		        				//suivi.append(i + " (null) : " + Chaine + "\n");
		        				etEnCours.setFichiermp3("");
		        				suivi.append("Mise à jour : Effacement du nom du fichier\n");
		        				suivi.setCaretPosition(suivi.getText().length());
		        				try {
		        					if (!simulation)  gestionBases.getInstance().majSon(etEnCours);
			        			} catch (Exception e1) {
			        				JOptionPane.showMessageDialog(null, 
			        						"Importer : Une erreur est intervenue lors de l'affacement du nom : " + e1.getLocalizedMessage() + "\nArrêt de la procédure", 
			        						"Correction", 
			        						JOptionPane.WARNING_MESSAGE, 
			        						null);
			        				return 0;
			        			}
		        				nbCor++;
		        			}
		    		}
					suivi.append("Nombre de traduction corrigée : " + nbCor + "\n");
					suivi.append("----------------------------------------------------\n");
		    		setCursor(Cursor.getDefaultCursor());
	    			JOptionPane.showMessageDialog(application, "Traitement de correction terminé avec succès\nNombre de traduction lues " + seance.getListe().size() + 
	    					                            "Nombre de correction apportées : " + nbCor.toString(),
	    					                            "Correction", JOptionPane.OK_OPTION);
		    	}
	    	}
	    	finally {
	    		setCursor(Cursor.getDefaultCursor());
	    	}
	    	return seance.getListe().size();
		}
    }

    public ficheCorrection(fenetrePrincipale app, Seance sc) {
		super();
		application = app;
		seance = sc;
        this.setTitle(constantes.getTitreAppli());
        this.setSize(550, 270);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Correction");
        this.setDefaultCloseOperation(JDialog .DISPOSE_ON_CLOSE);
        this.getContentPane().add( initComposants() );
		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
        this.pack();
	}
    private JPanel initComposants()
    {
        JPanel panNom = new JPanel();
        panNom.setBackground(Color.white);
        //panNom.setPreferredSize(new Dimension(400,80));
        panNom.setBorder(BorderFactory.createTitledBorder("Paramètres importation"));
        

        JPanel panelProgression = new JPanel();
        panelProgression.setLayout(new MigLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground( Color.blue );
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(750,25));
        //progressBar.setSize(30, 1600);
        
    	JCheckBox cb_simulation = new JCheckBox("Simulation");
    	cb_simulation.setActionCommand("testsimulation");
    	cb_simulation.setSelected(true);
    	
    	panelProgression.add(cb_simulation, "wrap");
        panelProgression.add(progressBar, BorderLayout.CENTER);

        JPanel panelSuivi = new JPanel();
    	MigLayout layoutSuivi = new MigLayout("", "[] 10 []", "[] 10 []");
    	panelSuivi.setLayout(layoutSuivi);
    	panelSuivi.setBorder(BorderFactory.createTitledBorder("Suivi des corrections"));
    	
		suivi = new JTextArea();
    	suivi.setLineWrap(true);
    	suivi.setEditable(false);
    	suivi.setWrapStyleWord(true);
		JScrollPane editSuiviScrollComm = new JScrollPane(suivi);
		editSuiviScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editSuiviScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		editSuiviScrollComm.setPreferredSize(new Dimension(720, 310));
		
		panelSuivi.add(editSuiviScrollComm, "wrap");

    	JPanel control = new JPanel();
    	control.setLayout(new MigLayout());
    	
    	okBouton = new JButton("Correction");
        okBouton.setActionCommand("correction");
        okBouton.addActionListener(this);
        this.getRootPane().setDefaultButton(okBouton);
        
        JButton cancelBouton = new JButton("Fermer");
        cancelBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        control.add(okBouton);
        control.add(cancelBouton);
        
        JPanel paneltot = new JPanel();
        MigLayout layouttot = new MigLayout("", "", "[] 0 [] 0 []");
        paneltot.setLayout(layouttot);
        paneltot.add(panelSuivi, "grow, wrap");
        paneltot.add(panelProgression, "grow, wrap");
        paneltot.add(control, "center");
        return paneltot;
    }
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("correction")) {
	    	new threadCorrection().execute();
		}
		if (e.getActionCommand().equals("simulation")) {
			simulation = !simulation;
		}
	}
	// Permet de quitter la fiche par la touche ECHAP
	private void onKeyEscape() {
		this.setVisible(false);
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
