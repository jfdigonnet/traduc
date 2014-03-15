package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import utilitaires.AudioFilePlayer;
import utilitaires.constantes;

import fiches.fenetrePrincipale;
import fiches.ficheAffiRecherche;

public class actionJouer implements ActionListener {

	private Component application;
	private String fichier = null;
	
    class MonSwingWorker extends SwingWorker<Integer, String> {

		protected Integer doInBackground() throws Exception {
	        final AudioFilePlayer player = new AudioFilePlayer ();
	    	player.play(constantes.getRepMP3() + fichier);
			return 0;
		}
    }
	/***********************************************************
	 * Jouer le fichier mp3 du mot en langue étrangère
	 ***********************************************************/
	public actionJouer(Component app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
        if (application instanceof fenetrePrincipale) {
        	fichier = (((fenetrePrincipale)application).getEtEnCours().getFichiermp3());
        }
        if (application instanceof ficheAffiRecherche) {
        	fichier = ((ficheAffiRecherche)application).getEtEnCours().getFichiermp3();
        }
    	System.out.println("On joue : " + fichier);
    	new MonSwingWorker().execute();
	}
}
