package metier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import utilitaires.AudioFilePlayer;
import utilitaires.MonSwingWorker;
import utilitaires.constantes;
import fiches.FicheInterro;
import fiches.fenetrePrincipale;

public class Interrogation implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	private FicheInterro ficheinterro;
	private String FichierSon;
	
	public Interrogation(fenetrePrincipale app, Seance sc) {
		this.application = app;
		this.seance = sc;
		ficheinterro = new FicheInterro(application);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		ficheinterro.setLocationRelativeTo(application);
		ficheinterro.getBoutonJouer().setActionCommand("jouer");
		ficheinterro.getBoutonJouer().addActionListener(this);
		ficheinterro.setVisible(true);
	}

	public void lanceInterrogation() {
		elementTraduc et = choisitTraduction();
		if ( et != null ) {
			ficheinterro.setLabelGB("");
			ficheinterro.setLabelF("");
			ficheinterro.getBoutonJouer().setToolTipText(et.getFichiermp3());
			FichierSon = et.getFichiermp3();
			joueSon();
			ficheinterro.setLabelGB(et.getAnglais().trim());
			ficheinterro.setLabelF(et.getFrancais().trim());
		}
	}
	/*
	 * On va tirer au sort une traduction
	 */
	private elementTraduc choisitTraduction() {
		elementTraduc etEnCours = null;
	    int noTraducEnCours = (int)Math.random() * seance.getListe().size();
		Random rand = new Random();
		int nombreAleatoire = rand.nextInt(seance.getListe().size()) + 1;
		System.out.println(nombreAleatoire);
		noTraducEnCours = nombreAleatoire;
		try {
			etEnCours = seance.loadTraduction();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application,
					"Erreur lors du chargement de la traduction no " + Integer.toString( noTraducEnCours ) + 
							e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
		return etEnCours;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("jouer")) {
			joueSon();
		}				
	}

	private void joueSon() {
		try {
			new MonSwingWorker(constantes.getRepMP3() + FichierSon).execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(application,
					"Erreur lors de la lecture du fichier sonore\n" +  
							e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
}
