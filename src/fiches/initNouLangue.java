package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class initNouLangue extends JDialog implements ActionListener {

	private     JTextField 			editL1;
	private     JTextField 			editL2;

	public initNouLangue() {
		JLabel texte1 = new JLabel("Cette transaction va supprimer toutes les données de l'application !");
		texte1.setBackground(Color.PINK );
		texte1.setForeground(Color.BLACK);
		texte1.setOpaque(true);
		texte1.setHorizontalAlignment(JTextField.CENTER);
		
		add(texte1, BorderLayout.NORTH);
		
		add(panneauLangue(), BorderLayout.CENTER );
		
		add(ajouteBoutons(), BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setSize(550,140);
	    setLocationRelativeTo(null);
	}
	public void actionPerformed(ActionEvent e) {
		if (editL1.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Le libellé de la langue 1 n'a pas été indiqué" , 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);	
			editL1.requestFocus();
			return;
		}
		if (editL2.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Le libellé de la langue 2 n'a pas été indiqué" , 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);	
			editL2.requestFocus();
			return;
		}
	}
	private Component ajouteBoutons() {
		JPanel panel = new JPanel();
		
		JButton boutonEnreg = new JButton("Enregistrer");
		boutonEnreg.setPreferredSize(new Dimension(150,25));
		boutonEnreg.addActionListener(this);
		boutonEnreg.setActionCommand("enreg");
		panel.add(boutonEnreg);

		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });
		panel.add(boutonQuitter);
		return panel;
	}
	private JPanel panneauLangue() {
		JPanel panelSup = new JPanel();
    	MigLayout layoutSup = new MigLayout("", "[] 10 [] 10 []", "[] 10 [] 10 []");
    	panelSup.setLayout(layoutSup);
		
	    setTitle("Libellé langues");

	    JLabel texte1 = new JLabel("Langue 1 : ");
	    editL1 = new JTextField(40);
	    editL1.setPreferredSize(new Dimension(30, 25));
		
	    JLabel texte2 = new JLabel("Langue 2 :");
	    editL2 = new JTextField(40);
	    editL2.setPreferredSize(new Dimension(30, 25));
		
	    panelSup.add(texte1);
		panelSup.add(editL1, "wrap");
	    panelSup.add(texte2);
		panelSup.add(editL2, "wrap");

		return panelSup;
	}
}
