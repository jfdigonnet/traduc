package fiches;
import java.io.File;

import javax.swing.filechooser.FileFilter;

import utilitaires.Utils;


public class filtreFichierSon extends FileFilter {

	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.mp3) || extension.equals(Utils.MP3) || extension.equals(Utils.ogg) || extension.equals(Utils.OGG)) 
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
		return "Fichiers sonores (mp3 ou MP3)";
	}
}
