package metier;

public class elementTraduc {
	
	private int id;
	private String anglais;
	private String francais;
	private String fichiermp3;
	private Boolean GBOk;
	private Boolean FOk;
	
	public String getAnglais() {
		return anglais;
	}
	public void setAnglais(String anglais) {
		this.anglais = anglais.replaceAll("''", "'");
	}
	public String getFrancais() {
		return francais;
	}
	public void setFrancais(String francais) {
		this.francais = francais.replaceAll("''", "'");
	}
	public String getFichiermp3() {
		return fichiermp3;
	}
	public void setFichiermp3(String fichiermp3) {
		System.out.println(fichiermp3);
		if (( fichiermp3.trim() != "null") && ( fichiermp3 != null)  && ( fichiermp3.trim().length() > 0) ) {  // AND
				this.fichiermp3 = fichiermp3;
		} else {
			this.fichiermp3 = "";
		}
	}
	public Boolean getGBOk() {
		return GBOk;
	}
	public void setGBOk(Boolean gbOk) {
		GBOk = gbOk;
	}
	public Boolean getFOk() {
		return FOk;
	}
	public void setFOk(Boolean fOk) {
		FOk = fOk;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getConnuGBSQLite() {
		if (this.GBOk)
			return "1";
		else
			return "0";
	}
	public String getConnuFSQLite() {
		if (this.GBOk)
			return "1";
		else
			return "0";
	}
	public void setGBOkSQLite(int parseInt) {
		if (parseInt == 0) 
			this.setGBOk(false);
		else
			this.setGBOk(true);
		
	}
	public void setFOkSQLite(int parseInt) {
		if (parseInt == 0) 
			this.setFOk(false);
		else
			this.setFOk(true);
		
	}
	public boolean getConnu( Boolean sens ) {
		if (sens) 
			return this.GBOk;
		else
			return this.FOk;
	}
	public String getFrancaisSQL() {
		return francais.replaceAll("'", "''");
	}
	public String getAnglaisSQL() {
		return anglais.replaceAll("'", "''");
	}
}
