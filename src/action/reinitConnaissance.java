package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import persistence.gestionBases;
import utilitaires.constantes;
import fiches.fenetrePrincipale;

public class reinitConnaissance implements ActionListener {

	private Component application;
	
	/***********************************************************
	* Suppression de la référence au son dans la table
	***********************************************************/
	public reinitConnaissance(Component app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
	    ((fenetrePrincipale)application).getEtEnCours().setFichiermp3("");
	    try {
	    	gestionBases.getInstance().reinitConnaissace();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de la réinitialisation dans la base\n :" +
					e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
