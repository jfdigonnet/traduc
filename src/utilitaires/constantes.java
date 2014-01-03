package utilitaires;

import java.io.File;

public class constantes {

	public static Integer nbcol = 25;
	public static String titreAppli = "Exercice de traduction";
	public static String repDonnees = "datatraduc";
	public static String repMP3 = repDonnees + File.separator + "mp3" + File.separator;
	public static String nomBase = repDonnees + File.separator + "traduc.db";
	public static String typeSGBD = "jdbc:sqlite:" + nomBase;
	private static String ExtensionSauvegarde = ".zip";
	public static String nomSauvegarde = "sauveTraduc" + ExtensionSauvegarde;
	public static String nomProgramme = "Exercice de traduction";
	public static String versionProgramme = "0.0.1";
	public static String dateProgramme = "01/01/2013";
	public static String[] tyepCh = {"Anglais", "Français"};
	public static String[] tyepSens = {"Anglais->Français", "Français->Anglais"};
	public static String[] tyepTri = {"Date de création (Du plus ancien au plus récent)", "Anglais", "Français", "Au hasard", "Date de création (Du plus récent au plus ancien)"};
	
	public static String getVersionProgramme() {
		return versionProgramme;
	}
	public static String getDateProgramme() {
		return dateProgramme;
	}
	public static String getTitreAppli() {
		return titreAppli;
	}
	public static String getRepDonnees() {
		return System.getProperty("user.dir") + File.separator + repDonnees;
	}
	public static void setRepDonnees(String repDonnees) {
		if (repDonnees.endsWith(File.separator))
			constantes.repDonnees = repDonnees;
		else
			constantes.repDonnees = repDonnees + File.separator;
	}
	public static String getRepMP3() {
		return System.getProperty("user.dir") + File.separator + repMP3;
	}
}
