package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

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
		application.getEtEnCours().setGBOk(application.getEditCheckGBOk());
		try {
			gestionBases.getInstance().modConnuGB(application.getEtEnCours());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement des données (GB) : " + e1.getLocalizedMessage(), 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);			
		}
	}

}
