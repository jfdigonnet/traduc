package metier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import param.parametres;
import persistence.gestionBases;

import java.util.Timer;

import utilitaires.MonSwingWorker;
import utilitaires.constantes;
import fiches.FicheInterro;
import fiches.fenetrePrincipale;

public class Interrogation {

	private fenetrePrincipale application;
	private ArrayList<Integer> liste;
	private FicheInterro ficheinterro;
	private elementTraduc etEnCours = null;
	private Timer timer;
	private Timer timerExec;
	private int noTraducEnCours;
	private int nbMots = 0;

	class MotNonConnu extends Thread {
		public void run() {
			try {
				gestionBases.getInstance().setMotAnglaisInconnu(etEnCours.getId());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement des données (" + paramLangues.getInstance().getLibLangue1() + ") : " + e1.getLocalizedMessage(), 
						"Enregistrement", 
						JOptionPane.ERROR_MESSAGE);			
			}
		}
	}
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
			timerExec.scheduleAtFixedRate(new NouvelleInterrogation(), parametres.getInstance().getTempsAvantInterrogationSuivante() * 1000, 5000);
			timer.cancel();
		}
	}
	/*
	 * On récupére les éléments de la séance en cours (la liste des mots)
	 * 
	 */
	public Interrogation(fenetrePrincipale app, ArrayList<Integer> ls) {
		this.application = app;
		this.liste = ls;
		ficheinterro = new FicheInterro(application);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		ficheinterro.setLocationRelativeTo(application);
		// On ajoute les évé sur les boutons
		ficheinterro.getBoutonQuitter().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	timer.cancel();
            	timerExec.cancel();
            	ficheinterro.dispose();
            }        
          });
		ficheinterro.getBoutonNonConnu().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	MotNonConnu t = new MotNonConnu() ;
            	t.start();
            }        
          });
		ficheinterro.setVisible(true);
	}
	public void lanceInterrogation() {
		etEnCours = choisitTraduction();
		if ( etEnCours != null ) {
			ficheinterro.setLabelGB("");
			ficheinterro.setLabelF("");
			ficheinterro.setLabelBas("Nombre de mots : " + nbMots);
			try {
				new MonSwingWorker(constantes.getRepMP3() + etEnCours.getFichiermp3()).execute();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(application,
						"Erreur lors de la lecture du fichier sonore\n" +  
								e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			}
			timer = new Timer();
			timer.scheduleAtFixedRate(new MonAction(), parametres.getInstance().getTempsAvantInterrogationSuivante() * 1000, 5000);
		}
	}
	/*
	 * On charge une traduction à partir de la base de données
	 */
	public elementTraduc loadTraduction(int no) throws Exception {
		elementTraduc traduc;
		traduc = gestionBases.getInstance().chargeUneTraduc( liste.get(no) );
		return traduc;
	}
	/*
	 * On va tirer au sort une traduction
	 */
	private elementTraduc choisitTraduction() {
		do {
			noTraducEnCours = (int)Math.random() * liste.size();
			Random rand = new Random();
			int nombreAleatoire = rand.nextInt(liste.size()) + 1;
			//System.out.println(nombreAleatoire);
			noTraducEnCours = nombreAleatoire;
			try {
				etEnCours = loadTraduction(noTraducEnCours);
				nbMots++;
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(application,
						"Erreur lors du chargement de la traduction no " + Integer.toString( noTraducEnCours ) + 
						e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			}
		} while (etEnCours.getFichiermp3().trim().length() == 0);
		return etEnCours;
	}
}
