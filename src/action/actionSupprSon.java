package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.Seance;

import persistence.gestionBases;

import utilitaires.constantes;

public class actionSupprSon implements ActionListener {

	private Component application;
	private Seance seance; 
	
	/***********************************************************
	* Suppression de la référence au son dans la table
	***********************************************************/
	public actionSupprSon(Component app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
	    seance.getEtEnCours().setFichiermp3("");
	    try {
	    	gestionBases.getInstance().supprimeSon(seance.getEtEnCours());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement dans la base\n :" +
					e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
