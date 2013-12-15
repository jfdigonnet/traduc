package fiches;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ficheStats extends JDialog {

	private 	JButton 	okBouton;
	private int[] resStat;
	
	public ficheStats(int[] res1) {
		this.resStat = res1;
        this.setTitle("Statistiques BD");
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog .DISPOSE_ON_CLOSE);
        this.getContentPane().add( initComposants() );
        // Bouton par défaut
        this.getRootPane().setDefaultButton(okBouton);

        pack();
		//setSize(540,270);
	    setLocationRelativeTo(null);
	}
	private JPanel initComposants() {
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
        panelres.add(label1);
        panelres.add(label1Res, "wrap");

        JLabel label2 = new JLabel("Nombre de traduction non maîtrisée (langue 1)");
        JLabel label2Res = new JLabel(String.valueOf(resStat[1]));
        panelres.add(label2);
        panelres.add(label2Res, "wrap");

        JLabel label3 = new JLabel("Nombre de traduction maîtrisée (langue 1)");
        JLabel label3Res = new JLabel(String.valueOf(resStat[2]));
        panelres.add(label3);
        panelres.add(label3Res, "wrap");

        
        JLabel label4 = new JLabel("Nombre de traduction non maîtrisée (langue 2)");
        JLabel label4Res = new JLabel(String.valueOf(resStat[3]));
        panelres.add(label4);
        panelres.add(label4Res, "wrap");
        
        JLabel label5 = new JLabel("Nombre de traduction maîtrisée (langue 2)");
        JLabel label5Res = new JLabel(String.valueOf(resStat[4]));
        panelres.add(label5);
        panelres.add(label5Res, "wrap");
        
        JLabel label6 = new JLabel("Nombre de traduction avec fichier sonore");
        float moyenne6 = 0;
        int a = resStat[5];
        int b = resStat[0];
        System.out.println(a);
        System.out.println(b);
        moyenne6 = (float)a / (float)b;
        System.out.println(moyenne6);
        moyenne6 = moyenne6  * 100;
        
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits ( 2 ) ;
        df.setDecimalSeparatorAlwaysShown ( true ) ; 
        
        JLabel label6Res = new JLabel(String.valueOf(resStat[5]) + " (" + df.format (moyenne6) + "%)");
        panelres.add(label6);
        panelres.add(label6Res, "wrap");
        
        //paneltot.add(content, "wrap");
        //paneltot.add(panelProgression, "grow, wrap");
        
        JPanel control = new JPanel();

        okBouton = new JButton("Fermer");
        getRootPane().setDefaultButton(okBouton);
        okBouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        control.add(okBouton);
        
        paneltot.add(panelres, "wrap, span, grow");
        paneltot.add(control, "center, width 120 ");

        return paneltot;        
	}
}
