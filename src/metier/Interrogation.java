package metier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import persistence.gestionBases;

import java.util.Timer;

import utilitaires.MonSwingWorker;
import utilitaires.constantes;
import fiches.FicheInterro;
import fiches.fenetrePrincipale;

public class Interrogation implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	private FicheInterro ficheinterro;
	private String FichierSon;
	private elementTraduc etEnCours = null;
	private Timer timer;
	private Timer timerExec;
	private int noTraducEnCours;

	class NouvelleInterrogation extends TimerTask {
		public void run() {
			lanceInterrogation();
			timerExec.cancel();
		}
	}
	class MonAction extends TimerTask {
		public void run() {
			String[] tab = etEnCours.getAnglais().split("\n");
			ficheinterro.setLabelGB(tab[0].trim());
			tab = etEnCours.getFrancais().split("\n");
			ficheinterro.setLabelF(tab[0].trim());
			timerExec = new Timer();
			timerExec.scheduleAtFixedRate(new NouvelleInterrogation(), 4000, 5000);
			timer.cancel();
		}
	}

	public Interrogation(fenetrePrincipale app, Seance sc) {
		this.application = app;
		this.seance = sc;
		ficheinterro = new FicheInterro(application);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		ficheinterro.setLocationRelativeTo(application);
//		ficheinterro.getBoutonJouer().setActionCommand("jouer");
//		ficheinterro.getBoutonJouer().addActionListener(this);
		ficheinterro.getBoutonQuitter().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	timer.cancel();
            	timerExec.cancel();
            	ficheinterro.setVisible(false);
            }        
          });
		ficheinterro.setVisible(true);
	}

	public void lanceInterrogation() {
		etEnCours = choisitTraduction();
		if ( etEnCours != null ) {
			ficheinterro.setLabelGB("");
			ficheinterro.setLabelF("");
//			ficheinterro.getBoutonJouer().setToolTipText(etEnCours.getFichiermp3());
			FichierSon = etEnCours.getFichiermp3();
			joueSon();
			timer = new Timer();
			timer.scheduleAtFixedRate(new MonAction(), 4000, 5000);
		}
	}
	public elementTraduc loadTraduction() throws Exception {
		elementTraduc traduc;
		traduc = gestionBases.getInstance().chargeUneTraduc( seance.getListe().get(noTraducEnCours) );
		return traduc;
	}
	/*
	 * On va tirer au sort une traduction
	 */
	private elementTraduc choisitTraduction() {
	    noTraducEnCours = (int)Math.random() * seance.getListe().size();
		Random rand = new Random();
		int nombreAleatoire = rand.nextInt(seance.getListe().size()) + 1;
		System.out.println(nombreAleatoire);
		noTraducEnCours = nombreAleatoire;
		try {
			etEnCours = loadTraduction();
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
