package utilitaires;

import javax.swing.SwingWorker;

public class MonSwingWorker extends SwingWorker<Integer, String> {
	
	private String nomFichierSon = "";

	public MonSwingWorker(String fichier) throws Exception {
		this.nomFichierSon = fichier;
	}
	protected Integer doInBackground() throws Exception {
        final AudioFilePlayer player = new AudioFilePlayer ();
        System.out.println("On joue : " + constantes.getRepMP3() + nomFichierSon);
    	player.play(constantes.getRepMP3() + nomFichierSon);
		return 0;
	}
}
