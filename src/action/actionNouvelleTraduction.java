package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fiches.fenetrePrincipale;
import fiches.ficheNouvelleTraduc;

public class actionNouvelleTraduction implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	 * Ajouter une traduction
	 ***********************************************************/
	public actionNouvelleTraduction(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheNouvelleTraduc fnt = new ficheNouvelleTraduc();
		fnt.setModal(true);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		fnt.setLocationRelativeTo(application);
		fnt.setVisible(true);
	}

}
