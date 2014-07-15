package fiches;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class FicheInterro extends JDialog {

//	private JButton boutonArreter;
//	private JButton boutonJouer;
	private JLabel labelGB;
	private JLabel labelF;
	private JButton boutonQuitter;
	
	public FicheInterro(fenetrePrincipale appl) {
		JPanel panelSup = new JPanel();
		MigLayout layout = new MigLayout("", "[]", "[] 15 [] 50 []");
		
    	panelSup.setLayout(layout);
    	
	    setTitle("Résultat de la recherche");

		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[]", "[] 50 [] 50 []"); // Colonne / Ligne
		panelS.setLayout(layouS);
		
		labelGB = new JLabel("Ici sera affiché le mot en langue 1", SwingConstants.CENTER);
		labelGB.setPreferredSize(new Dimension(250,25));
		labelF = new JLabel("Et ici sa traduction", SwingConstants.CENTER);
		labelF.setPreferredSize(new Dimension(250,25));

//		boutonJouer = new JButton("Jouer");
//		boutonJouer.setActionCommand("jouer");
//		//boutonJouer.addActionListener(this);
//		boutonJouer.setMnemonic( KeyEvent.VK_J ) ;
//		boutonJouer.setPreferredSize(new Dimension(380,25));

		panelS.add(labelGB, "wrap, center");
		panelS.add(labelF, "wrap, center");
//		panelS.add(boutonJouer);
		panelSup.add(panelS, "wrap");
		
//		boutonArreter = new JButton("Arrêter");
//		boutonArreter.setPreferredSize(new Dimension(150,25));

		boutonQuitter = new JButton("Fermer");
		getRootPane().setDefaultButton(boutonQuitter);
		boutonQuitter.setPreferredSize(new Dimension(150,25));


		JPanel panelB = new JPanel();
		MigLayout layoutB = new MigLayout();
		panelB.setLayout(layoutB);

//		panelB.add(boutonArreter);
		panelB.add(boutonQuitter, "center");
		
		panelSup.add(panelB);

		add(panelSup);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(650,200);
		pack();
	    setLocationRelativeTo(null);		
	}
	/**
	 * @param labelGB the labelGB to set
	 */
	public void setLabelGB(String texte) {
		this.labelGB.setText(texte);
	}
	/**
	 * @param labelF the labelF to set
	 */
	public void setLabelF(String texte) {
		this.labelF.setText(texte);
	}
//	/**
//	 * @return the boutonJouer
//	 */
//	public JButton getBoutonJouer() {
//		return boutonJouer;
//	}
	/**
	 * @return the boutonQuitter
	 */
	public JButton getBoutonQuitter() {
		return boutonQuitter;
	}
}

