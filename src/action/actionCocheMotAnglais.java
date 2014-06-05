package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.Seance;
import metier.paramLangues;

import persistence.gestionBases;

import fiches.fenetrePrincipale;

public class actionCocheMotAnglais implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	
	/***********************************************************
	 * L'utilisateur a coché / décoché la case connu
	 ***********************************************************/
	public actionCocheMotAnglais(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		if (application.getEditCheckGBOk()) {
			// La case est cochée
			seance.getEtEnCours().setGBOk(true);
			seance.setCochesseance(seance.getCochesseance() + 1);
		} else {
			// La case est décochée
			seance.getEtEnCours().setGBOk(false);
			seance.setCochesseance(seance.getCochesseance() - 1);
		}
		application.affiLigneStatus();
		try {
			gestionBases.getInstance().modConnuGB(seance.getEtEnCours());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement des données (" + paramLangues.getInstance().getLibLangue1() + ") : " + e1.getLocalizedMessage(), 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);			
		}
	}

}
