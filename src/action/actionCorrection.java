package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import metier.Seance;

import fiches.fenetrePrincipale;
import fiches.ficheCorrection;

public class actionCorrection implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;

	public actionCorrection(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		ficheCorrection fr = new ficheCorrection(application, seance);
		fr.setModal(true);
		fr.setVisible(true);
	}
}
