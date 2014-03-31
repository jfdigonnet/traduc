package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.paramLangues;

import persistence.gestionBases;

import fiches.fenetrePrincipale;

public class actionCocheMotAnglais implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	 * L'utilisateur a coché / décoché la case connu
	 ***********************************************************/
	public actionCocheMotAnglais(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		if (application.getEditCheckGBOk()) {
			application.getEtEnCours().setGBOk(true);
			application.setCochesseance(application.getCochesseance() + 1);
		} else {
			application.getEtEnCours().setGBOk(false);
			application.setCochesseance(application.getCochesseance() - 1);
		}
		application.affiLigneStatus();
		try {
			gestionBases.getInstance().modConnuGB(application.getEtEnCours());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement des données (" + paramLangues.getInstance().getLibLangue1() + ") : " + e1.getLocalizedMessage(), 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);			
		}
	}

}
