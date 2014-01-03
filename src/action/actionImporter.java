package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Import.FicheImportation;

import fiches.fenetrePrincipale;

public class actionImporter implements ActionListener {

	private fenetrePrincipale application;
	
	public actionImporter(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		FicheImportation ficheimport = new FicheImportation(application, "Importation", true);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		ficheimport.setLocationRelativeTo(application);
		ficheimport.setVisible(true);
	}

}
