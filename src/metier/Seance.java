package metier;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import param.parametres;
import persistence.gestionBases;
import utilitaires.constantes;

import fiches.fenetrePrincipale;

public class Seance {

	// Variables globales
	//Classe contenant une traduction
	private elementTraduc etEnCours;
	// Ensemble des id des traductions
	private ArrayList<Integer> liste;
	// GBVersF Donne le sens 0: GB vers F 1: F vers GB
	// No de la traduction en cours pour les deux sens 
	private int noTraducEnCours = -1;
	// Nombre de mots vu au cours de la seance
	private int vusseance = 0;
	private int cochesseance = 0;
	private int start = 0;
	private fenetrePrincipale application;
	
	public Seance(fenetrePrincipale fenetrePrincipale) {
		application = fenetrePrincipale;
	}

	/**
	 * On va charger une traduction dans la table
	 * On part du l'identifiant de la traduction (clé primaire)
	 * On renvoie une occurrence de l'objet elementTraduc
	 * @param traduc
	 * @return
	 * @throws Exception 
	 */
	public elementTraduc loadTraduction() throws Exception {
		elementTraduc traduc;
		traduc = gestionBases.getInstance().chargeUneTraduc( noTraducEnCours );
		return traduc;
	}
	private void chargeTraduction() {
		try {
			etEnCours = loadTraduction();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application,
					"Erreur lors du chargement de la traduction no " + Integer.toString( noTraducEnCours ) + 
					e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Affichage de la traduction précédente
	 */
	public void affichePrecedent() {
		if (noTraducEnCours > 0) {
			if (parametres.getInstance().getAfficherTousLesMots()) {
				decNoTraducEnCours();
				chargeTraduction();
			} else {
				int ancIndex = noTraducEnCours;
				do {
					decNoTraducEnCours();
					if (noTraducEnCours == 0) {
						noTraducEnCours = ancIndex;
						chargeTraduction();
						JOptionPane.showConfirmDialog(application, "Début de fichier atteint", constantes.titreAppli, JOptionPane.WARNING_MESSAGE);
					} else {
						chargeTraduction();
//							System.out.println(traducEnCours);
//							System.out.println( liste.size());
//							System.out.println(etEnCours.getConnu(GBVersF));
					}
				} while ((etEnCours.getConnu(parametres.getInstance().getSens()) && (noTraducEnCours > 0) ));
			}
		}
	}
	/**
	 * On incrémente le compteur de traduction
	 * On charge une traduction à partir de la table
	 * Si on veut toutes les voir : on l'affiche
	 * Sinon on regarde si elle est connue
	 * Si non on recommence
	 */
	public void afficheSuivant() {
		System.out.println( noTraducEnCours );
		System.out.println( liste.get( noTraducEnCours ) );
		System.out.println( liste.size());
		if (noTraducEnCours < liste.size()) {
			if (parametres.getInstance().getTypeTri() == 3) {
				    noTraducEnCours = (int)Math.random() * liste.size();
					Random rand = new Random();
					int nombreAleatoire = rand.nextInt(liste.size()) + 1;
					System.out.println(nombreAleatoire);
					noTraducEnCours = nombreAleatoire;
					try {
						etEnCours = loadTraduction();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(application,
								"Erreur lors du chargement de la traduction no " + Integer.toString( noTraducEnCours ) + 
										e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
					}
			} else {
				if (parametres.getInstance().getAfficherTousLesMots()) {
					noTraducEnCours++;
					chargeTraduction();
				} else {
					int ancIndex = noTraducEnCours;
					do {
						noTraducEnCours++;
						if (noTraducEnCours == liste.size() - 1) {
							noTraducEnCours = ancIndex;
							chargeTraduction();
							JOptionPane.showConfirmDialog(application, "Fin de fichier atteint", constantes.titreAppli, JOptionPane.WARNING_MESSAGE);
						} else {
							System.out.println(liste.get( noTraducEnCours ) );
							chargeTraduction();
						}
					} while ((etEnCours.getConnu(parametres.getInstance().getSens())) && (noTraducEnCours < (liste.size() - 1)));
				}
			}
		} else {
			noTraducEnCours = liste.size() - 1;
			chargeTraduction();
		}
	}
	
	public elementTraduc getEtEnCours() {
		return etEnCours;
	}
	public void setEtEnCours(elementTraduc etEnCours) {
		this.etEnCours = etEnCours;
	}
	public ArrayList<Integer> getListe() {
		return liste;
	}
	public void setListe(ArrayList<Integer> liste) {
		this.liste = liste;
	}
	public int getNoTraducEnCours() {
		return noTraducEnCours;
	}
	public void setNoTraducEnCours(int noTraducEnCours) {
		this.noTraducEnCours = noTraducEnCours;
	}
	public void incNoTraducEnCours() {
		this.noTraducEnCours = noTraducEnCours + 1;
	}
	public void decNoTraducEnCours() {
		this.noTraducEnCours = noTraducEnCours - 1;
	}
	public int getVusseance() {
		return vusseance;
	}
	public void incVusseance() {
		this.vusseance = vusseance + 1;
	}
	public void setVusseance(int vusseance) {
		this.vusseance = vusseance;
	}
	public int getCochesseance() {
		return cochesseance;
	}
	public void setCochesseance(int cochesseance) {
		this.cochesseance = cochesseance;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public String getLigneStatus() {
		return 	" Mot en cours  : " + (noTraducEnCours + 1) + 
				" sur " + (liste.size()) + 
				"  Mot dans la séance : " + vusseance + 
				" Acquis au cours de la séance : " + cochesseance;

	}
}
