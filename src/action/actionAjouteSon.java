package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import param.parametres;
import persistence.gestionBases;
import utilitaires.constantes;

import metier.Seance;
import fiches.fenetrePrincipale;
import fiches.filtreFichierSon;

public class actionAjouteSon implements ActionListener {

	private fenetrePrincipale application;
	private Seance seance;
	
	/***********************************************************
	 * Selectionner le fichier sonore
	 ***********************************************************/
	public actionAjouteSon(fenetrePrincipale app, Seance sc) {
		application = app;
		seance = sc;
	}
	public void actionPerformed(ActionEvent e) {
		selectionneFichierSonore();
	    application.adapteBouton();
	}
	/**
	 * On choisit le fichier sonore correspondant à la traduction
	 * On copie le fichier dans le dossier des sons
	 * et on enregistre l'information en base
	 */
	private void selectionneFichierSonore() {
		JFileChooser choixfichier = new JFileChooser();
		choixfichier.setDialogTitle("Choix du fichier sonore");
		// Empeche de pouvoir sélectionner Tous les fichiers
		choixfichier.setAcceptAllFileFilterUsed(false);
		choixfichier.setCurrentDirectory(new File(parametres.getInstance().loadParamRep()));
		choixfichier.setFileFilter(new filtreFichierSon());
		int returnVal = choixfichier.showOpenDialog(application);
		if ( returnVal == 0) {
			parametres.getInstance().sauveParamRep(choixfichier.getSelectedFile().getParent());
		    if ( choixfichier.getSelectedFile().getParent() + File.separator !=  constantes.getRepMP3() ) { 
			    // Copie du fichier dans le dossier des sons
				File ficIn = new File( choixfichier.getSelectedFile().getAbsolutePath()  );
				//System.out.println(constantes.getRepMP3() + choixfichier.getSelectedFile().getName());
				File ficOut = new File(constantes.getRepMP3() + choixfichier.getSelectedFile().getName());
				if (! ficIn.renameTo(ficOut)) {
					JOptionPane.showMessageDialog(application, "Erreur lors du déplacement du fichier\n" + ficIn.toString() + 
							" vers\n" + ficOut.toString(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
				}
		    }
		    seance.getEtEnCours().setFichiermp3( choixfichier.getSelectedFile().getName() );
		    try {
		    	gestionBases.getInstance().majSon( seance.getEtEnCours() );
			} catch (Exception e) {
				JOptionPane.showMessageDialog(application, "Erreur lors de l'enregistrement dans la base\n :" +
						e.getLocalizedMessage(), constantes.getTitreAppli(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
