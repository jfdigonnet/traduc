package Import;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import persistence.gestionBases;

import utilitaires.constantes;
import metier.elementTraduc;
import net.miginfocom.swing.MigLayout;

public class FicheImportation extends JDialog implements ActionListener, PropertyChangeListener {

	private 	JLabel 		labelNomFic;
    private 	JTextField 	fichierImport, repertoireImages;
    private 	JCheckBox 	checkBoxReinitBase;
    private 	JButton 	okBouton;
    private 	JProgressBar progressBar;
    private 	JPanel 		panelProgression;
    // Répertoire de la table
    private 	String 		repDB;
    private     ArrayList<String>   liste;

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
        /*
         * On lit le fichier cvs
         * Exemple de lecture : rosy;coloré, fleuri;";"
         * On décompose en deux
         * On enregistre
         * @see javax.swing.SwingWorker#doInBackground()
         */
		protected Integer doInBackground() throws Exception {
	    	Integer nbInteg = 0;
	    	progressBar.setMaximum(100);
	    	//System.out.println(liste.size());
	    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
        	if (checkBoxReinitBase.isSelected()) {
        		try {
        			gestionBases.getInstance().truncateBD();
				} catch (Exception e) {
					setCursor(Cursor.getDefaultCursor());
					JOptionPane.showMessageDialog(null, "Une erreur est intervenue lors de la suppression des données précédentes " + e.getMessage() + "\nArrêt du traitement d'intégration", "Erreur", JOptionPane.ERROR_MESSAGE);
					return 0;
				}
        	}
	    	try {
		    	String vaT = "";
		    	for(int i = 0 ; i <liste.size(); i++){
		    		  String maValeur = liste.get(i);
	    			  vaT  = vaT + maValeur + "\n";
	    			  if (vaT.split(";").length > 2) {
		    			  //System.out.println( vaT.split(";").length );
			    		  vaT = vaT.replace("\"","");
			    		  vaT = vaT.replace("'","''");
			    		  nbInteg++;
			    		  //System.out.println(nbInteg);
			    		  String str[] = vaT.split(";");
			  			  elementTraduc traduc = new elementTraduc();
						  traduc.setAnglais(str[0]);
                          traduc.setGBOk(false);
			    		  if (str.length > 1) {
							  traduc.setFrancais( str[1]);
			    		  }
			    		  else {
							  traduc.setFrancais( "" );
			    		  }
			  			try {
			  				gestionBases.getInstance().enreg(traduc);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement des données : " + e1.getLocalizedMessage(), 
									"Enregistrement", 
									JOptionPane.ERROR_MESSAGE);			
							}
			    		  vaT = "";
		    		  }
	    		 }
  			}
			catch (Exception e) {
							setCursor(Cursor.getDefaultCursor());
							JOptionPane.showMessageDialog(null, "Une erreur est intervenue lors de l'enregistrement des données : " + e.getMessage() + "\n" +  
									                            "\nArrêt du traitement d'intégration", "Erreur", JOptionPane.ERROR_MESSAGE);
							return nbInteg;
			}
    		// On met à jour le nombre de BD de la collection
    		setCursor(Cursor.getDefaultCursor());
   			JOptionPane.showMessageDialog(null, "Traitement d'intégration terminé avec succès\n" + 
   					                            "\nNombre de mots intégrés : " + nbInteg.toString(), 
   					                            "Intégration", JOptionPane.OK_OPTION);	 
	    	return nbInteg;
		}
    }

    public FicheImportation(JFrame parent, String title, boolean modal) {
        super(parent, modal);
        repDB = constantes.getRepDonnees();
        // On ajouter un slach en tant que dernier caractères si ce n'est pas le cas
        if ( repDB.substring(0, repDB.length()-1) != "/") repDB += "/"; 
        this.setTitle(title);
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
        panNom.setBorder(BorderFactory.createTitledBorder("Paramètres importation"));
        
        MigLayout layout = new MigLayout("", "[] 10 [] 0 []", "[] 10 [] 10 [] 0 []");
        panNom.setLayout(layout);
        fichierImport = new JTextField();
        fichierImport.setPreferredSize(new Dimension(400,25));
        labelNomFic = new JLabel("Fichier à importer");
        JButton boutonSelFic = new JButton("...");
        boutonSelFic.setActionCommand("selfic");
        boutonSelFic.addActionListener(this);
        
        panNom.add(labelNomFic);
        panNom.add(fichierImport);
        panNom.add(boutonSelFic, "wrap");

        checkBoxReinitBase = new JCheckBox();
        checkBoxReinitBase.setText("Réinitialiser les données");

        panNom.add(checkBoxReinitBase, "left");

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

        okBouton = new JButton("Importer");
        okBouton.setPreferredSize(new Dimension(120,25));
        okBouton.setActionCommand("importer");
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
    public void actionPerformed(ActionEvent e)
    {
    	/*
    	 * Sélection du fichier gcs ou xml contenant les données 
    	 * de CGStar
    	 */
        if (e.getActionCommand().equals("selfic") ) {
            JFileChooser choixfichier = new JFileChooser();
            choixfichier.setDialogTitle("Choix du fichier à intégrer");
            // Empeche de pouvoir sélectionner Tous les fichiers
            choixfichier.setAcceptAllFileFilterUsed(false);
            choixfichier.setCurrentDirectory(new File(System.getProperty("user.dir")));
            choixfichier.setFileFilter(new filtreFichierImport());
            int returnVal = choixfichier.showOpenDialog(this);
            if ( returnVal == 0) {
                File file = choixfichier.getSelectedFile();
                fichierImport.setText(file.toString());
            }
        }
        /*
         * Choix du dossier des images 
         * Cette information doit être saisie uniquement si le répertoire 
         * des images indiqués dans le XML n'est pas trouvé
         */
        if (e.getActionCommand().equals("selrep") ) {
        	choixRepertImage();
        }
        /*
         * Lancement de l'importation en elle même
         */
        if (e.getActionCommand().equals("importer") ) {
            if (fichierImport.getText().length() > 0) {
            	integ();
            } else {
                JOptionPane.showMessageDialog(this, "Le nom du fichier n'a pas été saisi", "Erreur", JOptionPane.ERROR_MESSAGE);
                fichierImport.requestFocus();
            }
        }
    }
	private void choixRepertImage() {
		JFileChooser choiceFile = new JFileChooser();
		choiceFile.setDialogTitle("Sélectionnez le répertoire des images");
		choiceFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = choiceFile.showOpenDialog(this);
		if ( returnVal == 0) {
		    File file = choiceFile.getSelectedFile();
		    repertoireImages.setText(file.toString());
		}
	}
	/*
	 * Chargement du fichier cvs dans un arraylist
	 */
	private ArrayList<String> LireFichier(String NomFichier) throws IOException {
		ArrayList<String> stuRec = new ArrayList<String>();
		InputStream ips = new FileInputStream(NomFichier); 
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String ligne;
		while ((ligne = br.readLine())!=null){
				//System.out.println(ligne);
				stuRec.add(ligne);
		}
		br.close(); 
		return stuRec;
	}
	/*
	 * Procédure d'intégration en elle même
	 * On crée la classe listeBD qui contiendra l'ensemble des informations contenu dans la sauvegarde de l'application GCStar
	 * On vérifie que si l'utilisateur veut charger les images qu'il ai bien 
	 * indiqué le dossie spécifié contient les couvertures des BD
	 * Si le dossier sélectionné est vide ou ne contient pas d'images on arrête la procédure
	 * Si l'utilisateur choisit de ne pas charger les images on ne fait donc pas cette vérification
	 */
    private void integ() {
    	try {
			liste = LireFichier(fichierImport.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	new threadImport().execute();
    }
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getNewValue());
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
