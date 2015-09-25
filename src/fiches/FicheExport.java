package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import persistence.gestionBases;
import metier.Seance;
import metier.elementTraduc;
import net.miginfocom.swing.MigLayout;
import utilitaires.constantes;

public class FicheExport extends JDialog implements ActionListener, PropertyChangeListener {

	private 	JLabel 		labelNomFic;
    private 	JTextField 	fichierExport;
    private 	JButton 	okBouton;
    private 	JProgressBar progressBar;
    private 	JPanel 		panelProgression;
    // Répertoire de la table
    private 	String 		repDB;
    private     ArrayList<Integer> liste;

    class threadImport extends SwingWorker<Integer, String> {

        public threadImport() {
            /* On ajoute un écouteur de barre de progression. */
            addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if("progress".equals(evt.getPropertyName())) {
                        progressBar.setValue((Integer) evt.getNewValue());
                    }
                }
            });
        }
		protected Integer doInBackground() throws Exception {
	    	Integer nbEcr = 0;
	    	progressBar.setMaximum(liste.size() - 1);
	    	progressBar.setValue(0);
	    	System.out.println(liste.size());
	    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
	    	// On crée le fichier
	    	String nomFichier = fichierExport.getText();
	    	if (!nomFichier.contains(".")) {
	    		nomFichier += ".csv";
	    	}
	    	FileWriter fw = new FileWriter(nomFichier, true);
	    	BufferedWriter output = new BufferedWriter(fw);
	    	try {
	    		System.out.println(liste.size());
		    	if (liste.size() > 0) {
		    		for (int i=0; i<(liste.size()); i++) {
		        			progressBar.setValue(i+1);
		        			//System.out.println(progressBar.getValue());
		        			//System.out.println(i);
		        			elementTraduc etEnCours = null;
		        			try {
		        				etEnCours = gestionBases.getInstance().chargeUneTraduc( liste.get(liste.get(i)) );
		        			} catch (Exception e1) {
		        				JOptionPane.showMessageDialog(null, 
		        						"Exporter : Une erreur est intervenue lors de la lecture de la table : " + e1.getLocalizedMessage() + "\n" +  
		        						"Indice de lecture : " + liste.get(liste.get(i)) + "\n" + 
		        						"Arrêt de la procédure", 
		        						"Exportation", 
		        						JOptionPane.WARNING_MESSAGE, 
		        						null);
		        				return 0;
		        			}
		        			output.write(etEnCours.getAnglais() + ";" + etEnCours.getFrancais() + ";" + etEnCours.getFichiermp3() + "\n");
		    		}
		    		output.flush();
		    		output.close();
		    		setCursor(Cursor.getDefaultCursor());
	    			JOptionPane.showMessageDialog(null, "Traitement d'exportation terminé avec succès\nNombre de traduction lues " + liste.size() + 
	    					                            "Nombre de traductions écrites : " + nbEcr.toString(),
	    					                            "Exportation", JOptionPane.OK_OPTION);
		    	}
	    	}
	    	finally {
	    		setCursor(Cursor.getDefaultCursor());
	    	}
	    	return liste.size();
		}
    }
    public FicheExport( fenetrePrincipale app, Seance seance ) {
    	liste = seance.getListe();
    	//System.out.println("Nombre d'articles : " + liste.size());
        repDB = constantes.getRepDonnees();
        // On ajouter un slach en tant que dernier caractères si ce n'est pas le cas
        if ( repDB.substring(0, repDB.length()-1) != "/") repDB += "/"; 
        this.setTitle("Exporter les données");
        this.setSize(550, 270);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
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
        panNom.setBorder(BorderFactory.createTitledBorder("Paramètres exportation"));
        
        MigLayout layout = new MigLayout("", "[] 10 [] 0 []", "[] 10 [] 10 [] 0 []");
        panNom.setLayout(layout);
        fichierExport = new JTextField();
        fichierExport.setPreferredSize(new Dimension(520,25));
        labelNomFic = new JLabel("Fichier pour l'export");
        JButton boutonSelFic = new JButton("...");
        boutonSelFic.setActionCommand("selfic");
        boutonSelFic.addActionListener(this);
        
        panNom.add(labelNomFic);
        panNom.add(fichierExport);
        panNom.add(boutonSelFic, "wrap");

        JPanel content = new JPanel();
        //MigLayout layoutcontent = new MigLayout();
        //content.setLayout(layoutcontent);
        content.setBackground(Color.white);
        content.add(panNom, "wrap");

        panelProgression = new JPanel();
        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground( Color.blue );
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(700,25));
        panelProgression.add(progressBar, BorderLayout.CENTER);
        progressBar.setSize(30, 1500);
        
        JPanel control = new JPanel();

        okBouton = new JButton("Exporter");
        okBouton.setPreferredSize(new Dimension(120,25));
        okBouton.setActionCommand("exporter");
        okBouton.addActionListener(this);
        JButton cancelBouton = new JButton("Fermer");
        cancelBouton.setPreferredSize(new Dimension(120,25));
        cancelBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dispose();
            }
        });
        control.add(okBouton);
        control.add(cancelBouton);
        
        JPanel paneltot = new JPanel();
        MigLayout layouttot = new MigLayout("", "", "[] 0 [] 0 []");
        paneltot.setLayout(layouttot);
        paneltot.add(content, "wrap");
        paneltot.add(panelProgression, "grow, wrap");
        paneltot.add(control, "center");
        return paneltot;
        //panNom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
    private void export() {
    	new threadImport().execute();
    }
	public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exporter") ) {
            if (fichierExport.getText().length() > 0) {
            	export();
            } else {
                JOptionPane.showMessageDialog(this, "Le nom du fichier n'a pas été saisi", "Erreur", JOptionPane.ERROR_MESSAGE);
                fichierExport.requestFocus();
            }
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
