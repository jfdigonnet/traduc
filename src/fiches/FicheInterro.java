package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import utilitaires.constantes;

import net.miginfocom.swing.MigLayout;

public class FicheInterro extends JDialog {

//	private JButton boutonArreter;
//	private JButton boutonJouer;
	private JLabel labelGB;
	private JLabel labelF;
	private JButton boutonQuitter;
	private JButton boutonNonConnu;
	private JLabel labelBas;
	
	public FicheInterro(fenetrePrincipale appl) {
		JPanel panelSup = new JPanel();
		MigLayout layout = new MigLayout("", "[]", "[] 15 [] 15 []");
		
    	panelSup.setLayout(layout);
    	
	    setTitle("Résultat de la recherche");

		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[]", "[] 30 [] 30 []"); // Colonne / Ligne
		panelS.setLayout(layouS);
		
		labelGB = new JLabel("Ici sera affiché le mot en langue 1", SwingConstants.CENTER);
		Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color( 133, 156, 221 ),new Color( 133, 156, 221 ));
		labelGB.setBorder(border);
		Dimension d = new Dimension(300,25);
		labelGB.setPreferredSize(d);
		labelGB.setMinimumSize(d);
		labelGB.setMaximumSize(d);
		labelF = new JLabel("Et ici sa traduction", SwingConstants.CENTER);
		labelF.setBorder(border);
		labelF.setPreferredSize(d);
		labelF.setMinimumSize(d);
		labelF.setMaximumSize(d);

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

		boutonQuitter = new JButton("Arrêter");		
		boutonQuitter.setPreferredSize(new Dimension(150,25));

		boutonNonConnu = new JButton("Inconnu");
		boutonNonConnu.setPreferredSize(new Dimension(150,25));
		getRootPane().setDefaultButton(boutonNonConnu);

		JPanel panelB = new JPanel();
		MigLayout layoutB = new MigLayout();
		panelB.setLayout(layoutB);

//		panelB.add(boutonArreter);
		panelB.add(boutonQuitter);
		panelB.add(boutonNonConnu);
		
		panelSup.add(panelB, "center");

		add(panelSup);
		
		labelBas = new JLabel();
		labelBas.setText(constantes.titreAppli);
//		labelBas.setBackground( new Color( 133, 156, 221 ) );
		Border myRaisedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color( 133, 156, 221 ),
				new Color( 133, 156, 221 ));
		//Border border = BorderFactory.createRaisedBevelBorder();
		labelBas.setBorder(myRaisedBorder);
		
		add(labelBas, BorderLayout.SOUTH);

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
	/**
	 * @param labelBas the labelBas to set
	 */
	public void setLabelBas(String labelBas) {
		this.labelBas.setText( labelBas );
	}
	/**
	 * @return the boutonNonConnu
	 */
	public JButton getBoutonNonConnu() {
		return boutonNonConnu;
	}
}

