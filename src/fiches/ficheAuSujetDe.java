package fiches;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

/*
 * On prend comme gestionnaire de layout le borderlayqout
 * car il va nous permettre d'afficher en haut un titre
 * à gauche une image et du texte au centre
 */
public class ficheAuSujetDe extends JDialog {
	
	public ficheAuSujetDe() {
	    setTitle("Au sujet de...");
	    this.getContentPane().setLayout(new BorderLayout());
	    
		JLabel titre =  new JLabel();
		Font police = new Font("Tahoma", Font.BOLD, 20);
		titre.setFont(police);
		titre.setPreferredSize(new Dimension(200,45));;
		titre.setHorizontalAlignment(JLabel.CENTER);
		titre.setText("Aide apprentissage de vocabulaire étranger");
		titre.setBorder(BorderFactory.createLineBorder( new Color( 133, 156, 221 ) ));
        add(titre, BorderLayout.NORTH);

        Icon icone = new ImageIcon("images/languages.png");
        JLabel labelIcone = new JLabel();
        labelIcone.setBackground( new Color( 133, 156, 221 ) );
        labelIcone.setOpaque(true);
        labelIcone.setIcon(icone);
        add(labelIcone, BorderLayout.WEST);

		JPanel panel1 = null;
		JPanel panel2 = null;
		JPanel panel3 = null;
		try {
			panel1 = createPage1();
			panel2 = createPage2();
			panel3 = createPage3();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Erreur lors de la construction de l'interface graphique :"
							+ e.getMessage());
		}

		// Create a tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Version", panel1);
		tabbedPane.addTab("GPL", panel2);
		tabbedPane.addTab("Informations", panel3);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

		add(tabbedPane, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
		JButton boutonQuitter = new JButton("Fermer");
		getRootPane().setDefaultButton(boutonQuitter);
		boutonQuitter.setMnemonic( KeyEvent.VK_F ) ;
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });
		panelSouth.add(boutonQuitter);
		panelSouth.setBorder(BorderFactory.createLineBorder(Color.black ));
		add(panelSouth, BorderLayout.SOUTH);
		
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setSize(400,300);
		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
		pack();
	    setLocationRelativeTo(null);
	}

	private JPanel createPage1() {
        JPanel panelDroit = new JPanel();
        panelDroit.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        MigLayout layout = new MigLayout("", "", "");
        panelDroit.setLayout(layout);
        panelDroit.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        MigLayout layoutd = new MigLayout("", "", "[] 20 [] 20 [] 20 []");
        panelDroit.setLayout(layoutd);

        JLabel texte1 = new JLabel("<html><b>Version</b> : " + utilitaires.constantes.getVersionProgramme());
        panelDroit.add(texte1, "wrap");
		
        JLabel texte2 = new JLabel("<html><b>Date</b> : " + utilitaires.constantes.getDateProgramme());
//        texte2.setBackground(Color.BLUE);
//        texte2.setOpaque(true);
        panelDroit.add(texte2, "wrap");

        JLabel texte3 = new JLabel("<html><b>Auteur</b> : JF Digonnet");
        panelDroit.add(texte3, "wrap");
        
        return panelDroit;
	}

	private JPanel createPage2() {
        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BorderLayout(10,10));
        JTextArea gpl = new JTextArea();
        readTextFile(gpl, "ressources/lgpl-3.0.txt");
        gpl.setEditable(false);
        // gestion auto des retours à la ligne
        gpl.setLineWrap(true);
        gpl.setWrapStyleWord(false);
        gpl.scrollRectToVisible(gpl.getVisibleRect());
        gpl.setCaretPosition(0);
        gpl.revalidate();
        JScrollPane lePanneau = new JScrollPane( gpl ) ;
        lePanneau.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        lePanneau.setPreferredSize(new Dimension(600, 400));
        panelCentre.add(lePanneau, BorderLayout.CENTER);
		return panelCentre;
	}
    public static String getJavaVendor() {
        String vd = System.getProperty("java.vm.vendor"); 
        if (vd == null || vd.trim().length() == 0) {
            vd = System.getProperty("java.vendor"); 
        }
        return System.getProperty("java.runtime.version") + " - " + vd;
    }
	private JPanel createPage3() {
        JPanel panelCentre = new JPanel();
        MigLayout layout = new MigLayout("", "[] 10 []", "[] 20 []");
        panelCentre.setLayout(layout);
        panelCentre.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        panelCentre.add(new JLabel("Os : "));
        panelCentre.add(new JLabel(System.getProperty ( "os.name" )), "wrap");
        panelCentre.add(new JLabel("Version :"));
        panelCentre.add(new JLabel(System.getProperty ( "os.version" )), "wrap");
	
        long TOTAL_MEMORY = Runtime.getRuntime().totalMemory();
        //long TOTAL_MEMORY_KB = TOTAL_MEMORY / 1024;
        long TOTAL_MEMORY_MB = TOTAL_MEMORY / 1048576;

        JLabel texte9 = new JLabel("Mémoire libre (en Mo) :");     
        long FREE_MEMORY = Runtime.getRuntime().freeMemory();
        //long FREE_MEMORY_KB = FREE_MEMORY / 1024;
        long FREE_MEMORY_MB = FREE_MEMORY / 1048576;
        
        long USED_MEMORY = TOTAL_MEMORY - FREE_MEMORY;
        //long USED_MEMORY_KB = USED_MEMORY / 1024;
        long USED_MEMORY_MB = USED_MEMORY / 1048576;

        panelCentre.add(new JLabel("Répertoire utilisateur :"));
        panelCentre.add(new JLabel(System.getProperty("user.dir")), "wrap");
        panelCentre.add(new JLabel("Java Home :"));
        panelCentre.add(new JLabel(System.getProperty("java.home")), "wrap");
        panelCentre.add(new JLabel("Version formatée de Java : "));
        panelCentre.add(new JLabel(System.getProperty("java.runtime.name") + " - " + getJavaVendor()), "wrap");
        

        panelCentre.add(new JLabel("Mémoire total (en Mo) :"));
        panelCentre.add(new JLabel(String.valueOf(TOTAL_MEMORY_MB)), "wrap");

        panelCentre.add(texte9);
        panelCentre.add(new JLabel(String.valueOf(FREE_MEMORY_MB)), "wrap");

        panelCentre.add(new JLabel("Mémoire libre :"));
        panelCentre.add(new JLabel(String.valueOf(USED_MEMORY_MB)), "wrap");

        panelCentre.add(new JLabel("Nombre de procésseurs :"));
        long NB_PROC = Runtime.getRuntime().availableProcessors();
        panelCentre.add(new JLabel(String.valueOf(NB_PROC)), "wrap");

        return panelCentre;
	}
	private void readTextFile(JTextArea texte, String fileName) 
 	{
 		try 
 			{
  			BufferedReader inStream  = new BufferedReader (new FileReader(fileName));
 			String line = inStream.readLine();  
 		 	while (line != null)
 		 	 {                        
     	       texte.append(line + "\n");                
		       line = inStream.readLine();                  
  			}
   			inStream.close();                              
  			} catch (Exception e) 
  				{
              texte.setText("Exception cause: "+e);
   		      e.printStackTrace();
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
