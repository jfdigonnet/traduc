package metier;

import fiches.FicheInterro;
import fiches.fenetrePrincipale;

public class Interrogation {

	private fenetrePrincipale application;
	
	public Interrogation(fenetrePrincipale app) {
		this.application = app;
		FicheInterro ficheinterro = new FicheInterro(application);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		ficheinterro.setLocationRelativeTo(application);
		ficheinterro.setVisible(true);
	}

	public void lanceInterrogation() {
		// TODO Auto-generated method stub
		
	}
}
