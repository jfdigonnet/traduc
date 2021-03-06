package fiches;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import param.parametres;
import persistence.gestionBases;
import utilitaires.Utils;

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
		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
		pack();
		setSize(550,140);
	    setLocationRelativeTo(null);
	}
	public void actionPerformed(ActionEvent e) {
		if (editL1.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Le libellé de la langue 1 n'a pas été indiqué" , 
					"Nouvelle langue", 
					JOptionPane.ERROR_MESSAGE);	
			editL1.requestFocus();
			return;
		}
		if (editL2.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Le libellé de la langue 2 n'a pas été indiqué" , 
					"Nouvelle langue", 
					JOptionPane.ERROR_MESSAGE);	
			editL2.requestFocus();
			return;
		}
    	int retour = JOptionPane.showConfirmDialog(this, "Confirmez-vous la ré initialisaition des données ?", "Nouvelle langue", JOptionPane.OK_CANCEL_OPTION); 
    	if ( retour == JOptionPane.OK_OPTION) {
			try {
				gestionBases.getInstance().reinitConnaissace();
				gestionBases.getInstance().truncateBD();
				System.out.println("Netoyage du répertoire");
				Utils.deleteDirectory(parametres.getInstance().getRepSons());
				JOptionPane.showMessageDialog(this, "Ré initailisation Ok", "Nouvelle langue", JOptionPane.OK_OPTION);
				dispose();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Une erreur est intervenue lors de la ré initialisation des données\n" + e1.getLocalizedMessage(), "Ré initilisation", JOptionPane.OK_OPTION);
			}
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
            	dispose();
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
