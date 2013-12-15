package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import persistence.gestionBases;

import utilitaires.constantes;

import fiches.fenetrePrincipale;
import fiches.ficheAffiRecherche;

public class actionSupprSon implements ActionListener {

	private Component application;
	
	/***********************************************************
	* Suppression de la référence au son dans la table
	***********************************************************/
	public actionSupprSon(Component app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		if ( application instanceof fenetrePrincipale) {
		    ((fenetrePrincipale)application).getEtEnCours().setFichiermp3("");
		    try {
		    	gestionBases.getInstance().supprimeSon(((fenetrePrincipale)application).getEtEnCours());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement dans la base\n :" +
						e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		}
		if ( application instanceof ficheAffiRecherche) {
		    ((fenetrePrincipale)application).getEtEnCours().setFichiermp3("");
		    try {
		    	gestionBases.getInstance().supprimeSon(((ficheAffiRecherche)application).getEtEnCours());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement dans la base\n :" +
						e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
