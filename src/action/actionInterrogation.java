package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fiches.fenetrePrincipale;

import metier.Interrogation;
import metier.Seance;

public class actionInterrogation implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance; 
	/*
	 * On lance une leçon : 
	 * 1) on prononce un mot en anglais
	 * 2) on l'affiche en anglais
	 * 3) on l'affiche en français 
	 */
	public actionInterrogation(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		Interrogation interro = new Interrogation(application, seance);
		interro.lanceInterrogation();
	}
}
