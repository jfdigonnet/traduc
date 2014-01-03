package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sauverestau.ficheSauvegarde;

import fiches.fenetrePrincipale;

public class actionSauvegarde implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	 * Réaliser une sauvegarde
	 ***********************************************************/
	public actionSauvegarde(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheSauvegarde fs = new ficheSauvegarde(application);
		fs.setModal(true);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		fs.setLocationRelativeTo(application);
		fs.setVisible(true);
	}

}
