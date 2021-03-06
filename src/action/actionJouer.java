package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import metier.Seance;

import utilitaires.AudioFilePlayer;
import utilitaires.constantes;

public class actionJouer implements ActionListener {

	private Seance seance;
	private String fichier = "";
	
    class MonSwingWorker extends SwingWorker<Integer, String> {

		protected Integer doInBackground() throws Exception {
	        final AudioFilePlayer player = new AudioFilePlayer ();
	    	player.play(fichier);
			return 0;
		}
    }
	/***********************************************************
	 * Jouer le fichier mp3 du mot en langue étrangère
	 ***********************************************************/
	public actionJouer(Seance sc) {
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
       	fichier = constantes.getRepMP3() + seance.getEtEnCours().getFichiermp3();
    	new MonSwingWorker().execute();
	}
}
