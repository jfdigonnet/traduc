package fiches;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utilitaires.constantes;

import metier.paramLangues;
import net.miginfocom.swing.MigLayout;

public class ficheRechercher extends JDialog implements ActionListener {

	private JTextField editCh;
	private JComboBox<String> comboTypeCh;
	private Boolean result; 
	
	public ficheRechercher() {
		result = false;
    	JPanel panelSup = new JPanel();
    	MigLayout layoutSup = new MigLayout("", "", "");
    	panelSup.setLayout(layoutSup);
    	
	    setTitle("Rechercher une traduction");
	    
		Color couleur = new Color(208, 255, 208);
		Container c = getContentPane();
		c.setBackground(couleur);
		
        JPanel panel = new JPanel();
        //MigLayout layout = new MigLayout("", "[] 10 [] 10 []", "[] 10 [] 10 []");
		panel.setLayout(new MigLayout("wrap 2","[] 50 []", "[] 25 [] 20 []"));

		JLabel label0 = new JLabel("Saisissez vos critères de recherche");
		panel.add(label0, "wrap");
		
		JLabel label1 = new JLabel();
		label1.setText("Rechercher dans :");
		
		JLabel label2 = new JLabel();
		label2.setText("Critère de recherche :");

		editCh = new JTextField();
		editCh.setBounds(10, 80, 150, 20);
//		editSerie.setFont(police);
		editCh.setPreferredSize(new Dimension(300, 25));

		panel.add(label1);
		
		comboTypeCh =  new JComboBox<String>();
		comboTypeCh.addItem(paramLangues.getInstance().getLibLangue1());
		comboTypeCh.addItem(paramLangues.getInstance().getLibLangue2());
	    comboTypeCh.setPreferredSize(new Dimension(150,25));
	    panel.add(comboTypeCh, "grow, wrap");

	    panel.add(label2);
		panel.add(editCh, "wrap");
		
		add(panel, BorderLayout.CENTER);
		
		JPanel panelSouth = new JPanel();
		JButton boutonCh = new JButton("Rechercher");
		getRootPane().setDefaultButton(boutonCh);
		boutonCh.setMnemonic( KeyEvent.VK_R ) ;
		boutonCh.setPreferredSize(new Dimension(150,25));
		boutonCh.addActionListener(this);
		panelSouth.add(boutonCh);

		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setMnemonic( KeyEvent.VK_F ) ;
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });
		panelSouth.add(boutonQuitter);
		add(panelSouth, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//setSize(500,300);
		pack();
	    //setLocationRelativeTo(null);
	    editCh.requestFocus();
	}
	public void actionPerformed(ActionEvent e) {
		if (editCh.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Le critère de recherche n'a pas été saisi", "Rechercher un mot", JOptionPane.WARNING_MESSAGE);
		} else {
			result = true;
			this.setVisible(false);
		}		
	}
	public Boolean getResult() {
		return result;
	}
	public String getEditCh() {
		return editCh.getText();
	}
	public int getComboTypeCh() {
		return comboTypeCh.getSelectedIndex();
	}
}
