package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sauverestau.ficheRestau;

import fiches.fenetrePrincipale;

public class actionRestaurer implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	 * Restaurer des données
	 ***********************************************************/
	public actionRestaurer(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheRestau fr = new ficheRestau();
		fr.setModal(true);
		fr.setVisible(true);
	}
}
