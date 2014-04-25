package Import;

import java.io.File;
import javax.swing.filechooser.*;

public class filtreFichierImport extends FileFilter
{
    public boolean accept(File f)
    {
        if (f.isDirectory()) {
            return true;
        } else {
        	return (f.getName().toLowerCase().endsWith(".csv") || f.getName().toLowerCase().endsWith(".txt"));
        }
//        String extension = Utils.getExtension(f);
//        if (extension != null) {
//        	if (extension.equals(Utils.csv) || extension.equals(Utils.CSV) || extension.equals(Utils.TXT) || extension.equals(Utils.txt))
//            {
//                    return true;
//            } else {
//                return false;
//            }
//        } 
//        return false;    
    }
    public String getDescription()
    {
        return "Fichiers CVS ou TXT";
    }

}
