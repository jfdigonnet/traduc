package sauverestau;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import utilitaires.ZIP;
import utilitaires.constantes;

import net.miginfocom.swing.MigLayout;

/*
 * Objet : Faire une sauvegarde de la base et des images
 * tar cvjf sauve.tar.gz .bdimages DBBD.db 
 */
public class ficheSauvegarde extends JDialog implements ActionListener {

	private JTextArea suivi;
	private Component application;
	
    class MonSwingWorker extends SwingWorker<Integer, String> {

		protected Integer doInBackground() throws Exception {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				File ficsauve = new File(constantes.nomSauvegarde);
				try {
					if ( ficsauve.exists()) {
						suivi.append("Suppression de la sauvegarde précédente\n");
						ficsauve.delete();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(application, 
							"Sauvegarde : Une erreur est intervenue lors de la suppression de la sauvegarde précédenté : " + e.getLocalizedMessage() + "\nArrêt de la procédure", 
							"Sauvegarde", 
							JOptionPane.WARNING_MESSAGE, 
							null); 
					return null;
				}
				ZIP zip = new ZIP(constantes.nomSauvegarde, constantes.repDonnees);
				if (zip.compress()) {
					suivi.append("Compression des fichiers Ok\n");
				} else {
					suivi.append("Erreur lors de la compression des fichiers\n");
				}
			} finally {
				setCursor(Cursor.getDefaultCursor());
			}
		return null;
		}
    }
	public ficheSauvegarde(Component app) {
		this.application = app;
    	MigLayout layoutSup = new MigLayout("", "", "");
    	getContentPane().setLayout(layoutSup);
    	
		JLabel label = new JLabel("Suivi :");
		label.setBounds(10, 60, 150, 20);

		suivi = new JTextArea(20,40);
    	suivi.setLineWrap(true);
    	suivi.setEditable(false);
    	suivi.setWrapStyleWord(true);
    	
    	JScrollPane listScrollComm = new JScrollPane(suivi);
		listScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
    	//suivi.setBorder(BorderFactory.createLineBorder(Color.gray ));
    	//suivi.setText("Suivi de la sauvegarde");

		JPanel panelSuivi = new JPanel();
    	MigLayout layoutSuivi = new MigLayout("", "", "");
    	panelSuivi.setLayout(layoutSuivi);
		panelSuivi.add(label, "wrap");
    	panelSuivi.add(listScrollComm);
    	 
    	add(panelSuivi, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();

		JButton boutonSauve = new JButton("Sauvegarde");
		boutonSauve.setPreferredSize(new Dimension(150,25));
		boutonSauve.addActionListener(this);
		boutonSauve.setActionCommand("sauve");
		panelSouth.add(boutonSauve);

		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
              setVisible(false);
            }        
          });
		panelSouth.add(boutonQuitter);
		add(panelSouth, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
		setSize(500,350);
		pack();
	    setLocationRelativeTo(null);
	}
	public void actionPerformed(ActionEvent e) {
		try {
			new MonSwingWorker().execute();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, 
					"Sauvegarde : Une erreur est intervenue " + e1.getLocalizedMessage() + "\nArrêt de la procédure", 
					"Sauvegarde", 
					JOptionPane.WARNING_MESSAGE, 
					null); 
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
