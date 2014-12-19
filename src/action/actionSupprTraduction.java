package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.Seance;

import persistence.gestionBases;

import utilitaires.constantes;

import fiches.fenetrePrincipale;

public class actionSupprTraduction implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	
	/***********************************************************
	* Suppression d'une traduction :
	* On mémorise l'index (id de la traduction)
	* On la supprime de la liste des id
	* Puis on supprime l'article de la base
	***********************************************************/
	public actionSupprTraduction(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		int retour = JOptionPane.showConfirmDialog(application, "Confirmez vous la suppression de cette traduction ?", 
				constantes.getTitreAppli(), 
				JOptionPane.OK_CANCEL_OPTION );
		if (retour == JOptionPane.OK_OPTION ) {
			// On mémorise l'id supprimé pour le supprimer 
			int ancIndex = seance.getListe().get(seance.getNoTraducEnCours());
			// On le supprime de la liste
			seance.getListe().remove(seance.getNoTraducEnCours());
			try {
				// On le supprime de la base de données
				gestionBases.getInstance().supprimeTraduction( ancIndex );
				// On recharge un element de traduction : celui qui occupe la place supprimé
				// Il faudra ajouter un test quand on supprime le dernier
				if (seance.getNoTraducEnCours() == seance.getListe().size() - 1) {
					seance.setNoTraducEnCours(seance.getListe().size() - 1);
				}
				seance.loadTraduction();
				application.soumettreTraduction();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(application,
						"Une erreur est intervenue lors de la suppression de la traduction " + "\n" + ex.getMessage(),
						constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
