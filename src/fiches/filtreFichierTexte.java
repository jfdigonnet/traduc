package fiches;


import java.io.File;

import javax.swing.filechooser.FileFilter;

import utilitaires.Utils;

public class filtreFichierTexte extends FileFilter {

	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.csv) || extension.equals(Utils.CSV) || extension.equals(Utils.TXT) || extension.equals(Utils.txt)) 
            {
                return true;
            } else {
                return false;
            }
        }
        return false;    
	}
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Fichiers texte (PNG ou JPG)";
	}
}
