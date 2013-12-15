package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import persistence.gestionBases;

import fiches.fenetrePrincipale;
import fiches.ficheAffiRecherche;
import fiches.ficheRechercher;

public class actionChercherUnMot implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	* Rechercher un mot
	***********************************************************/
	public actionChercherUnMot(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheRechercher ch = new ficheRechercher(); 
		ch.setModal(true);
		ch.setVisible(true);
		if (ch.getResult()) {
			System.out.println("Lancement de la recherche");
			String crit = ch.getEditCh();
			Integer typeCh = ch.getComboTypeCh();
			try {
				ArrayList<Integer> listeCh = gestionBases.getInstance().cherche(typeCh, crit);
				// On ne prend que le premier
				if ( listeCh.size() > 0 ) {
					ficheAffiRecherche far = new ficheAffiRecherche(listeCh);
					far.setModal(true);
					far.setVisible(true);
				}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(application, "Erreur lors de la recherche du mot : " + e1.getLocalizedMessage(), 
						"Recherche", 
						JOptionPane.ERROR_MESSAGE);			
			}
		}
		ch.dispose();
	}
}
