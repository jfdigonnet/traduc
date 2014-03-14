package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fiches.fenetrePrincipale;
import fiches.initNouLangue;

public class actioninitNouLangue implements ActionListener {

	public actioninitNouLangue(fenetrePrincipale fenetrePrincipale) {
		// TODO Auto-generated constructor stub
	}
	public void actionPerformed(ActionEvent e) {
		initNouLangue initnoulangue = new initNouLangue();
		initnoulangue.setVisible(true);
	}

}
