package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fiches.fenetrePrincipale;
import fiches.ficheAuSujetDe;

public class actionAuSujetDe implements ActionListener  {

	private fenetrePrincipale application;
	
	public actionAuSujetDe(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheAuSujetDe fr = new ficheAuSujetDe();
		fr.setModal(true);
		fr.setVisible(true);
	}
}
