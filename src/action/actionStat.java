package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import persistence.gestionBases;

import utilitaires.constantes;
import fiches.fenetrePrincipale;
import fiches.ficheStats;

public class actionStat implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	 * Afficher les statistiques
	 * On calcule les stats et on les affiche ensuite
	 ***********************************************************/
	public actionStat(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		int[] res1 = {0,0,0,0,0,0,0};
		try {
			gestionBases.getInstance().stats(res1);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(application,
					"Une erreur est intervenue lors de la confection des stats " + "\n" + ex.getMessage(),
					constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		}
		ficheStats fichestats = new ficheStats(res1);
		fichestats.setModal(true);
		// Centrer la fenêtre par rapport à la fenêtre principale 
		fichestats.setLocationRelativeTo(application);
		fichestats.setVisible(true);
	}		
}
