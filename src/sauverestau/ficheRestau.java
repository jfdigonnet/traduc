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

import utilitaires.ZIP;
import fiches.fenetrePrincipale;
import net.miginfocom.swing.MigLayout;

public class ficheRestau extends JDialog implements ActionListener {

	private JTextField editFichier;
	private JTextArea suivi;
	private fenetrePrincipale application;
	
    class threadSauvegarde extends SwingWorker<Integer, String> {

		protected Integer doInBackground() throws Exception {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			suivi.append("Restauration");
			try {
				String diskPath = System.getProperty("user.dir");
				File ficzip = new File(editFichier.getText());
				//ZIP zip = new ZIP(editFichier.getText(), diskPath);
				try {
					ZIP.uncompress(ficzip, new File(diskPath));
				}
				catch (Exception e) {
					suivi.append("Erreur restauration : " + e.getLocalizedMessage());
					JOptionPane.showMessageDialog(application, 
							"Une erreur est intervenue lors de la restauration : \n" + e.getLocalizedMessage() + "\nArrêt de la procédure", 
							"Restauration", 
							JOptionPane.WARNING_MESSAGE, 
							null); 
				}
			} finally {
				suivi.append("Fin de la restauration");
				setCursor(Cursor.getDefaultCursor());
			}
		return null;
		}
    }
	public ficheRestau(fenetrePrincipale app) {
		this.application = app;
    	MigLayout layoutSup = new MigLayout("", "", "[] 5 []");
    	getContentPane().setLayout(layoutSup);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelSouth = new JPanel();

		JLabel label = new JLabel("Sélection de la sauvegarde à utiliser :");
		label.setBounds(10, 60, 150, 20);

		JLabel label1 = new JLabel("Fichier :");
		label1.setBounds(10, 60, 150, 20);

		editFichier = new JTextField();
		//editFichier.setBounds(10, 80, 500, 20);
		editFichier.setPreferredSize(new Dimension(455, 25));

    	JButton boutonSel = new JButton("...");
    	boutonSel.setPreferredSize(new Dimension(50,25));
    	boutonSel.addActionListener(this);
    	boutonSel.setActionCommand("selection");
    	boutonSel.setToolTipText("Cliquez pour sélectionnez l'archive à utiliser");

		JPanel panelSuivi = new JPanel();
    	MigLayout layoutSuivi = new MigLayout("", "[] 10 []", "[] 10 []");
    	
    	panelSuivi.setLayout(layoutSuivi);
		panelSuivi.add(label, "span");
		panelSuivi.add(label1);
		panelSuivi.add(editFichier);
		panelSuivi.add(boutonSel, "wrap");

		suivi = new JTextArea(20,40);
    	suivi.setLineWrap(true);
    	suivi.setEditable(false);
    	suivi.setWrapStyleWord(true);
    	
		JLabel label2 = new JLabel("Suivi :");
		label2.setBounds(10, 60, 150, 20);

		JScrollPane listScrollComm = new JScrollPane(suivi);
		listScrollComm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollComm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		panelSuivi.add(label2);
		panelSuivi.add(listScrollComm, "wrap, span");

		JButton boutonRestaurer = new JButton("Restaurer les données");
		boutonRestaurer.setPreferredSize(new Dimension(200,25));
		boutonRestaurer.addActionListener(this);
		boutonRestaurer.setActionCommand("restaurer");
		panelSouth.add(boutonRestaurer);

		JButton boutonQuitter = new JButton("Fermer");
		boutonQuitter.setPreferredSize(new Dimension(200,25));
		boutonQuitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	dispose();
            }        
          });
		panelSouth.add(boutonQuitter);
		add(panelSuivi, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);

		// Permet de quitter la fiche par la touche ECHAP
		configureRootPane(this.getRootPane());
		pack();
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("restaurer")) {
				new threadSauvegarde().execute();
		}
		if (e.getActionCommand().equals("selection")) {
            JFileChooser choixfichier = new JFileChooser();
            choixfichier.setDialogTitle("Choix du fichier à restaurer");
            // Empeche de pouvoir sélectionner Tous les fichiers
            choixfichier.setAcceptAllFileFilterUsed(false);
            choixfichier.setCurrentDirectory(new File(System.getProperty("user.dir")));
            choixfichier.setFileFilter(new filtreFichierZIP());
            int returnVal = choixfichier.showOpenDialog(this);
            if ( returnVal == 0) {
                File file = choixfichier.getSelectedFile();
                editFichier.setText(file.toString());
                suivi.append("Fichier à utuliser" + editFichier.getText() + "\n");
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
