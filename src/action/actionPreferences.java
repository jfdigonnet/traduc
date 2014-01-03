package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import param.ficheParam;
import param.parametres;

import fiches.fenetrePrincipale;

public class actionPreferences implements ActionListener {

	private fenetrePrincipale application;
	
	/***********************************************************
	* Appel du menu préférences
	* Si on change l'ordre : il fau recharger les id et
	* remettre à zero le pointeur de lecture
    ***********************************************************/
	public actionPreferences(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheParam ficheparam = new ficheParam();
		ficheparam.setModal(true);
		ficheparam.setVisible(true);
		int anctri = parametres.getInstance().getTypeTri();
		Boolean ancsens = parametres.getInstance().getSens();
		application.majCheckBox();
		if ((anctri != parametres.getInstance().getTypeTri()) || (ancsens != parametres.getInstance().getSens())) { // OU
			application.chargementListeID();
			application.affichePremier();
		}
	}
}
