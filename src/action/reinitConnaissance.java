package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import metier.Seance;

import persistence.gestionBases;
import utilitaires.constantes;

public class reinitConnaissance implements ActionListener {

	private Component application;
	private Seance seance;
	
	/***********************************************************
	* Les rubriques ConnuGF et ConnuF sont remises à 0
	***********************************************************/
	public reinitConnaissance(Component app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
	    try {
	    	int retour = JOptionPane.showConfirmDialog(application, "Confirmez la ré initialisaition de la rubrique 'Connaissance'", "Réinitialisation", JOptionPane.OK_CANCEL_OPTION); 
	    	if ( retour == JOptionPane.OK_OPTION) {
		    	gestionBases.getInstance().reinitConnaissace();
		    	JOptionPane.showMessageDialog(application, "Traitement terminé", "Ré initilisation", JOptionPane.OK_OPTION);
	    	}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application, "Erreur lors de la réinitialisation dans la base\n :" +
					e1.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
