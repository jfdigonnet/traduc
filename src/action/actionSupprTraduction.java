package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import metier.Seance;
import metier.elementTraduc;

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
		// On récupère la liste de id
		ArrayList<Integer> liste = seance.getListe();
		// On mémorise l'id supprimé pour le supprimer 
		int ancIndex = liste.get(seance.getNoTraducEnCours());
		// On le supprime de la liste
		liste.remove(seance.getNoTraducEnCours());
		try {
			// On le supprime de la base de données
			gestionBases.getInstance().supprimeTraduction( ancIndex );
			// On recharge un element de traduction : celui qui occupe la place supprimé
			// Il faudra ajouter un test quand on supprime le dernier
			if (seance.getNoTraducEnCours() == liste.size()) {
				seance.setNoTraducEnCours(liste.size());
			}
			elementTraduc et = seance.loadTraduction();
			// On la mémorise
			seance.setEtEnCours( et );
			// On l'affiche
			application.soumettreTraduction( et );
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(application,
					"Une erreur est intervenue lors de la suppression de la traduction " + "\n" + ex.getMessage(),
					constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
