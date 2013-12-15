package sauverestau;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utilitaires.ZIP;

import net.miginfocom.swing.MigLayout;

public class ficheRestau extends JDialog implements ActionListener {

	private JTextField editFichier;
	private JTextArea suivi;
	
	public ficheRestau() {
    	MigLayout layoutSup = new MigLayout("", "", "[] 5 [] 5 []");
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
              setVisible(false);
            }        
          });
		panelSouth.add(boutonQuitter);
		add(panelSuivi, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);

		pack();
	    setLocationRelativeTo(null);		
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("restaurer")) {
			try {
				restaure();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, 
						"Importer : Une erreur est intervenue (0) " + e1.getLocalizedMessage() + "\nArrêt de la procédure", 
						"Restauration", 
						JOptionPane.WARNING_MESSAGE, 
						null); 
			}
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
	private void restaure() {
		// Répertoire courant
		String diskPath = System.getProperty("user.dir");
		File ficzip = new File(editFichier.getText());
		ZIP zip = new ZIP(editFichier.getText(), diskPath);
		try {
			zip.uncompress(ficzip, new File(diskPath));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
					"Une erreur est intervenue lors de la restauration : \n" + e.getLocalizedMessage() + "\nArrêt de la procédure", 
					"Restauration", 
					JOptionPane.WARNING_MESSAGE, 
					null); 
		}
	}
}
