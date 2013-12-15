package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utilitaires.AudioFilePlayer;
import utilitaires.constantes;

import fiches.fenetrePrincipale;
import fiches.ficheAffiRecherche;

public class actionJouer implements ActionListener {

	private Component application;
	
	/***********************************************************
	 * Jouer le fichier mp3 du mot anglais
	 ***********************************************************/
	public actionJouer(Component app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
        final AudioFilePlayer player = new AudioFilePlayer ();
        String fichier = null;
        if (application instanceof fenetrePrincipale) {
        	fichier = (((fenetrePrincipale)application).getEtEnCours().getFichiermp3());
        }
        if (application instanceof ficheAffiRecherche) {
        	fichier = ((ficheAffiRecherche)application).getEtEnCours().getFichiermp3();
        }
    	System.out.println("On joue : " + fichier);
    	player.play(constantes.getRepMP3() + fichier);
	}
}
