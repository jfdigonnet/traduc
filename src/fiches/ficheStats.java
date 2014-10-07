package fiches;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import metier.paramLangues;
import net.miginfocom.swing.MigLayout;

public class ficheStats extends JDialog {

	private 	JButton 	okBouton;
	private int[] resStat;
	
	public ficheStats(int[] res1) {
		this.resStat = res1;
        this.setTitle("Statistiques");
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog .DISPOSE_ON_CLOSE);
        this.getContentPane().add( initComposants() );
        // Bouton par défaut
        this.getRootPane().setDefaultButton(okBouton);

		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
        pack();
	}
	private JPanel initComposants() {
		Font police = new Font(Font.SANS_SERIF , Font.TRUETYPE_FONT, 40);
		this.setFont(police);

		Font police1 = new Font(Font.MONOSPACED, Font.PLAIN, 14);

		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
		df.setMinimumFractionDigits ( 2 ) ;
		df.setDecimalSeparatorAlwaysShown ( true ) ; 
		
        float moyenne = 0;
        int a = 0;
        int b = 0;

        JPanel paneltot = new JPanel();
        MigLayout layouttot = new MigLayout("", "", "[] 10 [] 0 []");
        paneltot.setLayout(layouttot);
        
        JPanel panelres = new JPanel();
        panelres.setBorder(BorderFactory.createTitledBorder("Stats"));
        MigLayout layoutres = new MigLayout("", "[] 100 []", "[] 10 []");
        panelres.setLayout(layoutres);

        JLabel label1 = new JLabel("Nombre total de traductions");
        label1.setSize(500, 50);
        JLabel label1Res = new JLabel(String.valueOf(resStat[0]));
        label1Res.setFont(police1);
        panelres.add(label1);
        panelres.add(label1Res, "wrap");

        JLabel labelSens1 = new JLabel("Sens " + paramLangues.getInstance().getLibLangue1() + " vers " + paramLangues.getInstance().getLibLangue2());
        panelres.add(labelSens1, "wrap");
        
        moyenne = 0;
        a = resStat[1];
        b = resStat[0];
        moyenne = (float)a / (float)b;
        moyenne = moyenne  * 100;

        JLabel label2 = new JLabel("Nombre de traduction non maîtrisée (langue " + paramLangues.getInstance().getLibLangue1() + ")");
        JLabel label2Res = new JLabel(String.valueOf(resStat[1]) + " (" + df.format (moyenne) + "%)");
        label2Res.setFont(police1);
        panelres.add(label2);
        panelres.add(label2Res, "wrap");

        moyenne = 0;
        a = resStat[2];
        b = resStat[0];
        moyenne = (float)a / (float)b;
        moyenne = moyenne  * 100;

        JLabel label3 = new JLabel("Nombre de traduction maîtrisée (langue " + paramLangues.getInstance().getLibLangue1() + ")");
        JLabel label3Res = new JLabel(String.valueOf(resStat[2]) + " (" + df.format (moyenne) + "%)");
        label3Res.setFont(police1);
        panelres.add(label3);
        panelres.add(label3Res, "wrap");

        
        JLabel labelSens2 = new JLabel("Sens " + paramLangues.getInstance().getLibLangue2() + " vers " + paramLangues.getInstance().getLibLangue1());
        panelres.add(labelSens2, "wrap");
        
        moyenne = 0;
        a = resStat[3];
        b = resStat[0];
        moyenne = (float)a / (float)b;
        moyenne = moyenne  * 100;

        JLabel label4 = new JLabel("Nombre de traduction non maîtrisée (langue " + paramLangues.getInstance().getLibLangue2() + ")");
        JLabel label4Res = new JLabel(String.valueOf(resStat[3]) + " (" + df.format (moyenne) + "%)");
        label4Res.setFont(police1);
        panelres.add(label4);
        panelres.add(label4Res, "wrap");
        
        moyenne = 0;
        a = resStat[4];
        b = resStat[0];
        moyenne = (float)a / (float)b;
        moyenne = moyenne  * 100;

        JLabel label5 = new JLabel("Nombre de traduction maîtrisée (langue " + paramLangues.getInstance().getLibLangue2() + ")");
        JLabel label5Res = new JLabel(String.valueOf(resStat[4]) + " (" + df.format (moyenne) + "%)");
        label5Res.setFont(police1);
        panelres.add(label5);
        panelres.add(label5Res, "wrap");
        
        JLabel labelSon = new JLabel("Fichiers sons");
        panelres.add(labelSon, "wrap");
        
        JLabel label6 = new JLabel("Nombre de traduction avec fichier sonore");
        moyenne = 0;
        a = resStat[5];
        b = resStat[0];
        moyenne = (float)a / (float)b;
        moyenne = moyenne  * 100;
        
        df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits ( 2 ) ;
        df.setDecimalSeparatorAlwaysShown ( true ) ; 
        
        JLabel label6Res = new JLabel(String.valueOf(resStat[5]) + " (" + df.format (moyenne) + "%)");
        label6Res.setFont(police1);
        panelres.add(label6);
        panelres.add(label6Res, "wrap");
        
        JLabel label7 = new JLabel("Nombre de traduction sans fichier sonore");

        moyenne = 0;
        int asons = resStat[5];
        int bsons = resStat[0];
        int moyennesons = bsons - asons;

        JLabel label7Res = new JLabel(String.valueOf(moyennesons));
        label7Res.setFont(police1);
        panelres.add(label7);
        panelres.add(label7Res, "wrap");
        //paneltot.add(content, "wrap");
        //paneltot.add(panelProgression, "grow, wrap");
        
        JPanel control = new JPanel();

        okBouton = new JButton("Fermer");
        getRootPane().setDefaultButton(okBouton);
        okBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dispose();
            }
        });
        control.add(okBouton);
        
        paneltot.add(panelres, "wrap, span, grow");
        paneltot.add(control, "center, width 120 ");

        return paneltot;        
	}
	// Permet de quitter la fiche par la touche ECHAP
	private void onKeyEscape() {
		this.dispose();
	}
	private void configureRootPane(JRootPane rootPane) {
	    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escPressed");
	 
	    rootPane.getActionMap().put("escPressed",
	        new AbstractAction("escPressed") {
	          public void actionPerformed(ActionEvent actionEvent) {
	            onKeyEscape();
	          }
	        });
	  }
}
