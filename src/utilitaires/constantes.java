package utilitaires;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;

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
	public static String[] tyepTri = {"Aucun", "Anglais", "Français"};
	
	public void centerOnScreen(final Component c, final boolean absolute) {
	    final int width = c.getWidth();
	    final int height = c.getHeight();
	    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screenSize.width / 2) - (width / 2);
	    int y = (screenSize.height / 2) - (height / 2);
	    if (!absolute) {
	        x /= 2;
	        y /= 2;
	    }
	    c.setLocation(x, y);
	}
	// Center on parent ( absolute true/false (exact center or 25% upper left) )
	public void centerOnParent(final Window child, final boolean absolute) {
	    child.pack();
	    boolean useChildsOwner = child.getOwner() != null ? ((child.getOwner() instanceof JFrame) || (child.getOwner() instanceof JDialog)) : false;
	    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    final Dimension parentSize = useChildsOwner ? child.getOwner().getSize() : screenSize ;
	    final Point parentLocationOnScreen = useChildsOwner ? child.getOwner().getLocationOnScreen() : new Point(0,0) ;
	    final Dimension childSize = child.getSize();
	    childSize.width = Math.min(childSize.width, screenSize.width);
	    childSize.height = Math.min(childSize.height, screenSize.height);
	    child.setSize(childSize);        
	    int x;
	    int y;
	    if ((child.getOwner() != null) && child.getOwner().isShowing()) {
	        x = (parentSize.width - childSize.width) / 2;
	        y = (parentSize.height - childSize.height) / 2;
	        x += parentLocationOnScreen.x;
	        y += parentLocationOnScreen.y;
	    } else {
	        x = (screenSize.width - childSize.width) / 2;
	        y = (screenSize.height - childSize.height) / 2;
	    }
	    if (!absolute) {
	        x /= 2;
	        y /= 2;
	    }
	    child.setLocation(x, y);
	}
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
