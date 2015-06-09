package sauverestau;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import fiches.fenetrePrincipale;
import param.parametres;
import utilitaires.ZIP;
import utilitaires.constantes;
import net.miginfocom.swing.MigLayout;

/*
 * Objet : Faire une sauvegarde de la base et des images
 * tar cvjf sauve.tar.gz .bdimages DBBD.db 
 */
public class ficheSauvegarde extends JDialog implements ActionListener {

	private JTextArea suivi;
	private fenetrePrincipale application;
	private JTextField editRep;

    class threadSauvegarde extends SwingWorker<Integer, String> {

		protected Integer doInBackground() throws Exception {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			suivi.append("La sauvegarde sera effectuée dans le dossier : " + editRep.getText().trim());
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
				ZIP zip = new ZIP(editRep.getText().trim() + '/' + constantes.nomSauvegarde, constantes.repDonnees);
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
	public ficheSauvegarde(fenetrePrincipale app) {
		this.application = app;
    	MigLayout layoutSup = new MigLayout("", "", "");
    	getContentPane().setLayout(layoutSup);
    	
		JLabel label1 = new JLabel("Répertoire :");
		label1.setBounds(10, 60, 150, 20);

		editRep = new JTextField();
		//editRep.setBounds(10, 80, 500, 20);
		editRep.setPreferredSize(new Dimension(455, 25));
		editRep.setText(parametres.getInstance().loadDerRepSauve());
		
    	JButton boutonSel = new JButton("...");
    	boutonSel.setPreferredSize(new Dimension(50,25));
    	boutonSel.addActionListener(this);
    	boutonSel.setActionCommand("selection");
    	boutonSel.setToolTipText("Cliquez pour sélectionnez l'archive à utiliser");

        JPanel panNom = new JPanel();
    	MigLayout layoutNuo = new MigLayout("", "", "");
    	panNom.setLayout(layoutNuo);
        //panNom.setBackground(Color.white);
        //panNom.setPreferredSize(new Dimension(400,80));
        panNom.setBorder(BorderFactory.createTitledBorder("Répertoire de sauvegarde"));
        panNom.add(label1);
        panNom.add(editRep);
        panNom.add(boutonSel);
        
        add(panNom, "wrap");
    	
		suivi = new JTextArea();
    	suivi.setLineWrap(true);
    	suivi.setEditable(false);
    	suivi.setWrapStyleWord(true);
    	
    	JScrollPane listScrollComm = new JScrollPane(suivi);
		listScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listScrollComm.setPreferredSize(new Dimension(590, 200));
		
    	//suivi.setBorder(BorderFactory.createLineBorder(Color.gray ));
    	//suivi.setText("Suivi de la sauvegarde");

		JPanel panelSuivi = new JPanel();
    	MigLayout layoutSuivi = new MigLayout("", "", "");
    	panelSuivi.setBorder(BorderFactory.createTitledBorder("Suivi"));
    	panelSuivi.setLayout(layoutSuivi);
    	panelSuivi.add(listScrollComm);
    	 
    	add(panelSuivi, "wrap, grow");

        JPanel panelSouth = new JPanel();
    	MigLayout layoutS = new MigLayout("fillx,insets 0");
    	panelSouth.setLayout(layoutS);

		JButton boutonSauve = new JButton("Sauvegarde");
		//boutonSauve.setPreferredSize(new Dimension(150,25));
		boutonSauve.addActionListener(this);
		boutonSauve.setActionCommand("sauve");
		panelSouth.add(boutonSauve, "width 150!");

		JButton boutonQuitter = new JButton("Fermer");
		//boutonQuitter.setPreferredSize(new Dimension(150,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	dispose();
            }        
          });
		panelSouth.add(boutonQuitter, "width 150!");
		add(panelSouth, "right");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
		setSize(500,350);
		pack();
	    setLocationRelativeTo(null);
	}
	public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand().equals("selection")) {
    	    JFileChooser jfc = new JFileChooser();
    	    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	    int returnVal = jfc.showOpenDialog(this);
            if ( returnVal == 0) {
                File file = jfc.getSelectedFile();
                editRep.setText(file.toString());
            }
    	}
    	if (e.getActionCommand().equals("sauve")) {
            if (editRep.getText().length() > 0) {
    			try {
    				new threadSauvegarde().execute();
    				parametres.getInstance().sauveDerRepSauve(editRep.getText().trim());
    			} catch (Exception e1) {
    				JOptionPane.showMessageDialog(this, 
    						"Sauvegarde : Une erreur est intervenue " + e1.getLocalizedMessage() + "\nArrêt de la procédure", 
    						"Sauvegarde", 
    						JOptionPane.WARNING_MESSAGE, 
    						null); 
    			}
            } else {
                JOptionPane.showMessageDialog(this, "Le nom du fichier n'a pas été saisi", "Erreur", JOptionPane.ERROR_MESSAGE);
                editRep.requestFocus();
            }
    	}
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
