package fiches;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class FicheInterro extends JDialog {

	private JButton boutonArreter;
	private JButton boutonJouer;
	private JLabel labelGB;
	private JLabel labelF;
	
	public FicheInterro(fenetrePrincipale appl) {
		JPanel panelSup = new JPanel();
		MigLayout layout = new MigLayout("", "[]", "[] 15 [] 50 []");
		
    	panelSup.setLayout(layout);
    	
	    setTitle("Résultat de la recherche");

		JPanel panelS = new JPanel();
		MigLayout layouS = new MigLayout("", "[]", "[] 50 [] 50 []"); // Colonne / Ligne
		panelS.setLayout(layouS);
		
		labelGB = new JLabel("Ici sera affiché le mot en langue 1");
		labelGB.setPreferredSize(new Dimension(150,25));
		labelF = new JLabel("Et ici sa traduction");
		labelF.setPreferredSize(new Dimension(150,25));

		boutonJouer = new JButton("Jouer");
		boutonJouer.setActionCommand("jouer");
		//boutonJouer.addActionListener(this);
		boutonJouer.setMnemonic( KeyEvent.VK_J ) ;
		boutonJouer.setPreferredSize(new Dimension(380,25));

		panelS.add(labelGB, "wrap, center");
		panelS.add(labelF, "wrap, center");
		panelS.add(boutonJouer);
		panelSup.add(panelS, "wrap");
		
		boutonArreter = new JButton("Arrêter");
		boutonArreter.setPreferredSize(new Dimension(150,25));

		JButton boutonQuitter = new JButton("Fermer");
		getRootPane().setDefaultButton(boutonQuitter);
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });


		JPanel panelB = new JPanel();
		MigLayout layoutB = new MigLayout();
		panelB.setLayout(layoutB);

		panelB.add(boutonArreter);
		panelB.add(boutonQuitter, "tag cancel");
		
		panelSup.add(panelB, "center");

		add(panelSup);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setSize(400,255);
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

	/**
	 * @return the boutonJouer
	 */
	public JButton getBoutonJouer() {
		return boutonJouer;
	}

	/**
	 * @return the boutonLancer
	 */
}

