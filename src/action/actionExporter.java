package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import metier.Seance;
import fiches.FicheExport;
import fiches.fenetrePrincipale;

public class actionExporter implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	
	public actionExporter(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		FicheExport ficheexport = new FicheExport( application, seance );
		// Centrer la fenêtre par rapport à la fenêtre principale 
		ficheexport.setLocationRelativeTo(application);
		ficheexport.setModal(true);
		ficheexport.setVisible(true);
	}
}
