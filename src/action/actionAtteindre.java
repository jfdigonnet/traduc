package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fiches.fenetrePrincipale;
import fiches.ficheAtteindre;

public class actionAtteindre implements ActionListener  {

	private fenetrePrincipale application;
	
	public actionAtteindre(fenetrePrincipale app) {
		application = app;
	}
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * On saisit le no de la traduction que l'on souhaire atteindre
	 */
	public void actionPerformed(ActionEvent e) {
		// On récupère la liste de id
		ArrayList<Integer> liste = application.getListe();
		// On mémorise l'id supprimé pour le supprimer 
		int ancIndex = liste.get(application.getNoTraducEnCours());

		ficheAtteindre att = new ficheAtteindre( liste.size() );
		att.setModal(true);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		att.setLocationRelativeTo(application);
		att.setVisible(true);
		if (att.getResultat()) {
			application.setNoTraducEnCours( att.getValeur() - 1 );
			application.setEtEnCours( application.loadTraduction( liste.get(ancIndex) ));
		    application.soumettreTraduction( application.getEtEnCours() );
		}
	}	
}
