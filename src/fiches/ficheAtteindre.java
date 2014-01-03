package fiches;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import param.parametres;

import net.miginfocom.swing.MigLayout;


public class ficheAtteindre extends JDialog implements ActionListener {

	private JSpinner editNbCh;
	private Boolean resultat = false;
	
	public ficheAtteindre( int max ) {
		Integer dernier = parametres.getInstance().loadParamDerAtteindre();
		JPanel panel = new JPanel();
    	MigLayout layout = new MigLayout("", "[] 10 [] 10 []", "[] 10 [] 10 []");
    	panel.setLayout(layout);
    	panel.setBorder(BorderFactory.createTitledBorder("Atteindre"));

	    setTitle("Atteindre");

		Integer value1 = new Integer(dernier);
		Integer min1 = new Integer(0);
		Integer max1 = max;
		Integer step1 = new Integer(1);
		SpinnerNumberModel model1 = new SpinnerNumberModel(value1, min1, max1, step1);
		editNbCh = new JSpinner(model1);
		editNbCh.setEditor(new JSpinner.NumberEditor(editNbCh, "#"));
		editNbCh.setPreferredSize(new Dimension(30, 25));
		
		JLabel texteD = new JLabel("No de traduction recherchée : ");
		panel.add(texteD, "align label");

		panel.add(texteD);
		panel.add(editNbCh);
		
        JPanel panelSouth = new JPanel();
    	MigLayout layoutSuivi = new MigLayout();
    	panelSouth.setLayout(layoutSuivi);
        
		JButton boutonEnreg = new JButton("Atteindre");
		boutonEnreg.setPreferredSize(new Dimension(150,25));
		boutonEnreg.addActionListener(this);
		boutonEnreg.setActionCommand("ok");
		getRootPane().setDefaultButton(boutonEnreg);
		
		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });

		panelSouth.add(boutonEnreg);
		panelSouth.add(boutonQuitter);

		JPanel panelFonds = new JPanel();
    	MigLayout layoutFonds = new MigLayout();
    	panelFonds.setLayout(layoutFonds);

		panelFonds.add(panel, "wrap");
		panelFonds.add(panelSouth);
		
		this.add(panelFonds);
		pack();
		editNbCh.requestFocus();
	}
	public Boolean getResultat() {
		return resultat;
	}
	public int getValeur() {
		return (Integer)editNbCh.getValue();
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			parametres.getInstance().sauveParamAtteindre((Integer)editNbCh.getValue());
			resultat = true;
			setVisible(false);
		}
	}
}
