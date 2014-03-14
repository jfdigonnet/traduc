package utilitaires;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;

public class Utils
{
    public final static String txt = "txt";
    public final static String csv = "csv";
    public final static String TXT = "TXT";
    public final static String CSV = "CSV";
    public final static String zip = "zip";
    public final static String ZIP = "ZIP";
    public final static String mp3 = "mp3";
    public final static String MP3 = "MP3";
	public final static String ogg = "ogg";
	public final static String OGG = "OGG";
 
    /* 
     * Chargement des param proxy
     * A mettre dans la page param
     */
    public static void chargeParamProxy() {
    	System.setProperty("http.proxyHost", "proxy.bercy.cp");
    	System.setProperty("http.proxyPort", "3128");
    }
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    public static void getFile(String host, String nomImage) throws Exception
    {
    	chargeParamProxy();
    	InputStream input = null;
    	FileOutputStream writeFile = null;

    	URL url = new URL(host);
    	URLConnection connection = url.openConnection();
    	int fileLength = connection.getContentLength();

    	if (fileLength == -1)
    	{
    		System.out.println("Invalide URL or file.");
    		return;
    	}

    	input = connection.getInputStream();
    	String fileName = nomImage;// url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
    	File file = new File(fileName);
    	if (file.exists()) file.delete();
    	System.out.println(fileName);
    	writeFile = new FileOutputStream(fileName);
    	byte[] buffer = new byte[1024];
    	int read;

    	while ((read = input.read(buffer)) > 0)
    		writeFile.write(buffer, 0, read);
    	writeFile.flush();

    	writeFile.close();
    	input.close();
    }
    public static void getURL(String url) throws Exception, IOException {
         chargeParamProxy();
  		  BufferedReader urlReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
  		  FileWriter localFile = new FileWriter(new File("fichier"));
   
  		  String s;
   
  		  while((s = urlReader.readLine()) != null){
  		     localFile.write(s);
  		  }
  		  urlReader.close();
  		  localFile.close();
    }
    static public boolean deleteDirectory(String repSon) { 
        boolean resultat = true; 
        File rep = new File(repSon);
        if( rep.exists() ) { 
                File[] files = rep.listFiles(); 
                for(int i=0; i<files.length; i++) { 
                        if(files[i].isDirectory()) { 
                                resultat &= deleteDirectory(files[i].getAbsolutePath()); 
                        } 
                        else { 
                        	resultat &= files[i].delete(); 
                        } 
                } 
        } 
        resultat &= rep.delete(); 
        return( resultat ); 
    } 
}
