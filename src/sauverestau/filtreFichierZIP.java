package sauverestau;
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class filtreFichierZIP extends FileFilter {

	@Override
	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
        	return (f.getName().toLowerCase().endsWith(".zip"));
        }
//        String extension = Utils.getExtension(f);
//        if (extension != null) {
//            if (extension.equals(Utils.zip) ||  extension.equals(Utils.ZIP)) {
//                    return true;
//            } else {
//                return false;
//            }
//        } 
//        return false;    
	}

	@Override
	public String getDescription() {
        return "Fichiers ZIP";
	}

}
