package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import metier.Seance;

import fiches.fenetrePrincipale;
import fiches.ficheAtteindre;

public class actionAtteindre implements ActionListener  {

	private fenetrePrincipale application;
	private Seance seance;
	
	public actionAtteindre(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * On saisit le no de la traduction que l'on souhaire atteindre
	 */
	public void actionPerformed(ActionEvent e) {
		ficheAtteindre att = new ficheAtteindre( seance.getListe().size() );
		att.setModal(true);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		att.setLocationRelativeTo(application);
		att.setVisible(true);
		if (att.getResultat()) {
			seance.setNoTraducEnCours( att.getValeur() - 1 );
			try {
				seance.setEtEnCours( seance.loadTraduction());
			} catch (Exception e1) {
				// Il faut lever une exception ici pour la transmettre à la fiche principale
				e1.printStackTrace();
			}
			application.soumettreTraduction( seance.getEtEnCours() );
		}
	}	
}
