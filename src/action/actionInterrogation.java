package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fiches.fenetrePrincipale;

import Import.FicheImportation;

import metier.Interrogation;
import metier.Seance;

public class actionInterrogation implements ActionListener {

	private fenetrePrincipale application;
	/*
	 * On lance une leçon : 
	 * 1) on prononce un mot en anglais
	 * 2) on l'affiche en anglais
	 * 3) on l'affiche en français 
	 */
	public actionInterrogation(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		Interrogation interro = new Interrogation(application);
		interro.lanceInterrogation();
	}
}
