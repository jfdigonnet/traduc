package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.Seance;
import metier.elementTraduc;

import persistence.gestionBases;

import fiches.fenetrePrincipale;
import fiches.ficheAffiRecherche;

public class actionEnregistrerTraduction implements ActionListener {

	private Component application;
	private Seance seance;
	
	/***********************************************************
	 * Enregistrer une traduction existante
	 ***********************************************************/
	public actionEnregistrerTraduction(Component app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		elementTraduc et = null;
		et = seance.getEtEnCours();
		if (application instanceof fenetrePrincipale) {
			et.setAnglaisSQL(((fenetrePrincipale)application).getEditGB().getText() );
			et.setFrancaisSQL(((fenetrePrincipale)application).getEditF().getText());
			et.setGBOk(((fenetrePrincipale)application).getEditCheckGBOk());
			et.setFOk(((fenetrePrincipale)application).getEditCheckFOk());
		}
		if (application instanceof ficheAffiRecherche) {
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
