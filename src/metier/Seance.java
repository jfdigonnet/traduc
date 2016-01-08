package metier;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
	// Mesure du temps passé
	long startTime = 0;
	long elapsedTime = 0;
	
	public Seance(fenetrePrincipale fenetrePrincipale) {
		startTime = System.currentTimeMillis();
		application = fenetrePrincipale;
		setNoTraducEnCours( parametres.getInstance().getPositionTraduction() );
		// On mémorise pour pouvoir recommencer la séance
		setStart( getNoTraducEnCours() );
		chargeLibelleLangues();
	}
	public String duree() {
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 0 ) ; //arrondi à 2 chiffres apres la virgules
		df.setDecimalSeparatorAlwaysShown ( false ) ; 
		return df.format( elapsedTime / 1000 );
	}
	/*
	 * On charge la liste de clé primaires des traductions dans une liste d'entier
	 * On charge le type de tri pour présenter les données
	 * Les types de tri sont listés dans le tableau tyepTri 
	 * On peut trier les mots :
	 * "Date de création (Du plus ancien au plus récent)"
	 * "Langue étrangère"
	 * "Langue maternelle"
	 * "Au hasard"
	 * "Date de création (Du plus récent au plus ancien)"
	 */
	public Boolean chargementListeID() {
		try {
			liste = gestionBases.getInstance().listeMots(parametres.getInstance().getTypeTri());
			//System.out.println("Nombre d'articles : " + liste.size());
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(application,
							"Erreur lors du chargement de la liste des traductions :" + 
							e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	/**
	 * @param et
	 * On affiche la traduction du mot en cours
	 * On a affiché un mot en GB ou en F et on affiche sa traduction dans l'autre langue
	 * Du français si on est dans le sens GB -> F
	 * et inversement
	 */
	public void affichageTraduc() {
		if (parametres.getInstance().getSens()) {
			application.getEditF().setText(etEnCours.getFrancais());
			application.getEditF().requestFocus();
			application.getEditF().setCaretPosition(0);
		} else {
			application.getEditGB().setText(etEnCours.getAnglais());
			application.getEditGB().requestFocus();
			application.getEditGB().setCaretPosition(0);
		}
		application.getMenuEnreg().setEnabled(true);
		application.getBoutonEnreg().setEnabled(true);
	}
	/*
	 * On charge dans la table des paramètres les libellés des langues 1 et 2
	 * Ces libellés sont toujours là car ils sont obligatoires dans le module
	 * (à écrire) d'initialisation d'un nouveau module de langue
	 */
	private void chargeLibelleLangues() {
		 try {
			gestionBases.getInstance().litLibelleLangues();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(application,
					"Erreur lors du chargement des libellés des langues :" + 
					e.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * On va charger une traduction dans la table
	 * On part du l'identifiant de la traduction (clé primaire)
	 * On renvoie une occurrence de l'objet elementTraduc
	 * @param traduc
	 * @return
	 * @throws Exception 
	 */
	public void loadTraduction() {
		try {
			etEnCours = gestionBases.getInstance().chargeUneTraduc( liste.get(noTraducEnCours) );
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application,
					"Erreur lors du chargement de la traduction no " + Integer.toString( noTraducEnCours ) + 
					e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	/*
	 *  On affiche le premier élémenent de la liste des mots
	 *  Si ce mot est connu et que l'on a choisi de n'afficher que les mot non connu
	 *  on se place sur le suivant jusqu'à ce que l'on trouve un mot non connu
	 */
	
	public void affichePremier() {
		noTraducEnCours = 0;
		loadTraduction();
		// Test type de lecture : Si pas tous les mots donc seulement les mots non connus
		if (! parametres.getInstance().getAfficherTousLesMots()) {
			do {
				noTraducEnCours++;
				if (noTraducEnCours == liste.size()) {
					noTraducEnCours--;
				}
				loadTraduction();
			} while ( (etEnCours.getConnu( parametres.getInstance().getSens()) ) && (noTraducEnCours < (liste.size() - 1)));
			application.soumettreTraduction();
		}
	}
	/*
	 * Affihchage du dernier élément de la liste 
	 * ou du précédent s'il est connu et que l'on ne veut que 
	 * les mots non connus
	 */
	public void afficheDernier() {
		noTraducEnCours = liste.size() - 1;
		loadTraduction();
		// Test type de lecture : Si pas tous les mots donc seulement les mots non connus
		if (! parametres.getInstance().getAfficherTousLesMots()) {
			if (etEnCours.getConnu( parametres.getInstance().getSens())) {
				do {
					noTraducEnCours--;
					if (noTraducEnCours == -1) {
						noTraducEnCours = 0;
					}
					loadTraduction();
				} while ( (etEnCours.getConnu( parametres.getInstance().getSens()) ) && (noTraducEnCours > -1) );
			}
		}
		application.soumettreTraduction();
	}
	public void afficheUneTraduction(int sens) throws Exception, SQLException {
		// Cas du tirage au sort : on n'est donc jamais au début ou à la fin du fichier
		if (parametres.getInstance().getTypeTri() == 3) {
			tirageAuSort();
		}
		// il n'y a pas d'autres mots à afficher
		if (! existeEncoreDesMotsNonConnus()) {
			JOptionPane.showMessageDialog(application,
					"Vous avez terminé\nTous les mots sont connus", constantes.titreAppli, JOptionPane.INFORMATION_MESSAGE );
		} else {
			if (sens == 0) {
				afficheSuivant();
			} else {
				affichePrecedent();
			}
		}
		application.soumettreTraduction();
	}
	/**
	 * Affichage de la traduction précédente
	 * @throws SQLException 
	 */
	public void affichePrecedent() throws Exception {
		// Cas où on affiche tous les mots : on sait qu'il existe au moins deux mots non connus
		if ( parametres.getInstance().getAfficherTousLesMots()) {
			reduitNoTraducEnCours();
			loadTraduction();
		} else {
			// On n'affiche que les mots non connus
			reduitNoTraducEnCours();
			loadTraduction();
			// On teste si le mot est connu
			if ( etEnCours.getConnu( parametres.getInstance().getSens()) ) {
				do {
					reduitNoTraducEnCours();
					loadTraduction();
				}
				while (etEnCours.getConnu(parametres.getInstance().getSens()) );
			}
		}
	}
	/**
	 * On réduit le no de la traduction en cours de 1
	 * Si on est revenu au début du fichier, on repart à la fin
	 * On peut donc continuer à réduire
	 */
	private void reduitNoTraducEnCours() {
		// Si on n'est pas au début
		if (noTraducEnCours > 0) {
			noTraducEnCours--;
		} else {
			// Sinon on va à la fin
			noTraducEnCours = liste.size() - 1;
		}
	}
	/**
	 * On réduit le no de la traduction en cours de 1
	 * Si on est revenu au début du fichier, on repart à la fin
	 * On peut donc continuer à réduire
	 */
	private void augmenteNoTraducEnCours() {
		// Si on n'est pas au début
		if (noTraducEnCours < liste.size() - 1) {
			noTraducEnCours++;
		} else {
			// Sinon on va à la fin
			noTraducEnCours = 0;
		}
	}
	public void tirageAuSort() {
	    noTraducEnCours = (int)Math.random() * liste.size();
		Random rand = new Random();
		int nombreAleatoire = rand.nextInt(liste.size()) + 1;
		System.out.println(nombreAleatoire);
		noTraducEnCours = nombreAleatoire;
		try {
			loadTraduction();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(application,
					"Erreur lors du chargement de la traduction no " + Integer.toString( noTraducEnCours ) + 
							e1.getMessage(), constantes.titreAppli, JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * On incrémente le compteur de traduction
	 * On charge une traduction à partir de la table
	 * Si on veut toutes les voire : on l'affiche
	 * Sinon on regarde si elle est connue
	 * Si non on recommence
	 * @throws SQLException 
	 * @throws HeadlessException 
	 */
	public void afficheSuivant() throws Exception {
		if ( parametres.getInstance().getAfficherTousLesMots()) {
			augmenteNoTraducEnCours();
			loadTraduction();
		} else {
			augmenteNoTraducEnCours();
			loadTraduction();
			// On teste si le mot est connu
			if ( etEnCours.getConnu( parametres.getInstance().getSens()) ) {
				do {
					augmenteNoTraducEnCours();
					loadTraduction();
				}
				while (etEnCours.getConnu(parametres.getInstance().getSens()) );
			}
		}
	}
	private boolean existeEncoreDesMotsNonConnus() throws SQLException {
		return gestionBases.getInstance().existeEncoreDesMotsNonConnus(parametres.getInstance().getSens());
	}
	public elementTraduc getEtEnCours() {
		return etEnCours;
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
