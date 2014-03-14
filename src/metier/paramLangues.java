package metier;

public class paramLangues {

	// Libellés des langues
	private String libLangue1 = "";
	private String libLangue2 = "";
	
	private static paramLangues instance = new paramLangues();

	public static paramLangues getInstance() {
        return instance;
    }

	public String getLibLangue1() {
		return libLangue1;
	}

	public void setLibLangue1(String libLangue1) {
		this.libLangue1 = libLangue1;
	}

	public String getLibLangue2() {
		return libLangue2;
	}

	public void setLibLangue2(String libLangue2) {
		this.libLangue2 = libLangue2;
	}
}
