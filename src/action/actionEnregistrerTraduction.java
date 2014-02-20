package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.elementTraduc;

import persistence.gestionBases;

import fiches.fenetrePrincipale;
import fiches.ficheAffiRecherche;

public class actionEnregistrerTraduction implements ActionListener {

	private Component application;
	
	/***********************************************************
	 * Enregistrer une traduction existante
	 ***********************************************************/
	public actionEnregistrerTraduction(Component app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		elementTraduc et = null;
		if (application instanceof fenetrePrincipale) {
			et = ((fenetrePrincipale)application).getEtEnCours();
			et.setAnglaisSQL(((fenetrePrincipale)application).getEditGB());
			et.setFrancaisSQL(((fenetrePrincipale)application).getEditF());
			et.setGBOk(((fenetrePrincipale)application).getEditCheckGBOk());
			et.setFOk(((fenetrePrincipale)application).getEditCheckFOk());
		}
		if (application instanceof ficheAffiRecherche) {
			et = ((ficheAffiRecherche)application).getEtEnCours();
			et.setAnglaisSQL(((ficheAffiRecherche)application).getEditGB());
			et.setFrancaisSQL(((ficheAffiRecherche)application).getEditF());
		}
		try {
			gestionBases.getInstance().mod(et);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement des données : " + e1.getLocalizedMessage(), 
					"Enregistrement", 
					JOptionPane.ERROR_MESSAGE);			
		}
	}
}
