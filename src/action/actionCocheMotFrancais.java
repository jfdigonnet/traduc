package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.Seance;

import persistence.gestionBases;

import fiches.fenetrePrincipale;

public class actionCocheMotFrancais implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	
	/***********************************************************
	 * L'utilisateur a coché / décoché la case connu
	 ***********************************************************/
	public actionCocheMotFrancais(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		seance.getEtEnCours().setFOk(application.getEditCheckFOk());
		try {
			gestionBases.getInstance().modConnuF(seance.getEtEnCours());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement des données (F) : " + e1.getLocalizedMessage(), 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);			
		}
	}
}
