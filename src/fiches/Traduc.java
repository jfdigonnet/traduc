package fiches;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import persistence.gestionBases;

public class Traduc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (gestionBases.getInstance().testDB()) {
			JFrame fen = new fenetrePrincipale() ;
			//fen.setBounds (100, 40, 300, 200) ;
			fen.setVisible (true) ;
		} else {
			JOptionPane.showMessageDialog(null,
					"Erreur connexion a la base de données\n Abandon du programme\nVérifiez la présence de la base",
					"Exercice de traduction", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
