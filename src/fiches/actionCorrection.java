package fiches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class actionCorrection implements ActionListener {

	private fenetrePrincipale application;

	public actionCorrection(fenetrePrincipale app) {
		application = app;
	}
	public void actionPerformed(ActionEvent e) {
		ficheCorrection fr = new ficheCorrection(application);
		fr.setModal(true);
		fr.setVisible(true);
	}
}
