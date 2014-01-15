package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import metier.elementTraduc;

import utilitaires.constantes;

public class gestionBases
{
	private static gestionBases instance = new gestionBases();
	private Connection connection = null;
	private java.sql.Statement statement = null;

	private gestionBases() {
        try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("Base sur : " + constantes.typeSGBD);
			connection = DriverManager.getConnection(constantes.typeSGBD);
	        statement = connection.createStatement();
	        statement.setQueryTimeout(30);  // set timeout to 30 sec.
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static gestionBases getInstance() {
        return instance;
    }
    /**********************************************************************
     * Test de la connexion
     **********************************************************************/
    public String testLaConnexion() {
    	try
    	{
    		//ResultSet ping = null;
    		//ping = statement.executeQuery("select * from traduction LIMIT 1");
    		statement.executeQuery("select 1");
    		//return ping.next();
    		statement.close();
    		return "";
    	}
    	catch(Exception e)
    	{
    		System.err.println(e.getMessage());
    		return e.getMessage();
    	}
    }
    /**********************************************************************
     * Vidage de la base
     **********************************************************************/
    public void truncateBD() throws Exception {
            String query = "delete from traduction";
            System.out.println(query);
            statement.executeUpdate(query);
        	statement.close();
    }
    /**********************************************************************
     * Test accés à la base SQLITE
     **********************************************************************/
    public boolean testDB()
    {
        gestionBases testConnex;
        try
        {
            testConnex = new gestionBases();
            testConnex.testLaConnexion(); 
//            if ( Message.length() > 0)  {
//                JOptionPane.showMessageDialog(this, "Erreur connexion à la base de données BDDB :" + Message);
//            }
            return true;
        }
        catch (Exception e1)
        {
//            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion à la base de données BDDB " + e1.getMessage());
            return false;
        }
    }
    /**********************************************************************
     * 
     **********************************************************************/
    public Integer NouId() throws Exception {
    	int id = -1;
    	String query = "";
    	query = "select max(id) as maxi from traduction";
    	System.out.println(query);
    	ResultSet rs = statement.executeQuery( query );
    	while (rs.next()) {
    		id = rs.getInt("maxi");
    	}
    	System.out.println("Ancien id " + id);
    	System.out.println("Nou id " + (id + 1));
    	rs.close();
    	statement.close();
    	return id + 1;
    }
    /**********************************************************************
     * 
     * @param gb
     * @param f
     * @param fichierSon
     * @param connu
     * @throws Exception
     **********************************************************************/
    public void enreg(elementTraduc et) throws Exception  {
    	String query = "";
    	query += "INSERT INTO traduction ";
    	query += " ('id', 'anglais', 'francais','fichiermp3', 'connuGB', 'connuF') ";
    	query +=	"VALUES (";
    	query += NouId() + ", ";
    	query += "'" + et.getAnglaisSQL() + "', ";
    	query += "'" + et.getFrancaisSQL() + "', ";
    	query += "'" + et.getFichiermp3() + "',";
    	query += et.getConnuGBSQLite() + ",";
    	query += et.getConnuFSQLite() + ")";
    	System.out.println(query);
    	statement.executeUpdate(query);
    	statement.close();
    }
    /**********************************************************************
     * 
     * @param et
     * @throws Exception
     **********************************************************************/
    public void mod(elementTraduc et) throws Exception  {
    	String query = "UPDATE traduction ";
    	query += "set anglais = '" + et.getAnglais() + "', ";
    	query += " francais='" + et.getFrancaisSQL() + "', ";
    	query += " fichiermp3 = '" +  et.getFichiermp3() + "', ";
    	query += " connuGB = '" + et.getConnuGBSQLite() + "',";
    	query += " connuF = '" + et.getConnuFSQLite() + "' ";
    	query += " WHERE ID ='" + et.getId()  + "'";

    	System.out.println(query);
    	statement.executeUpdate(query);
    	statement.close();
    }
    /**********************************************************************
     * Chargement d'une traduction
     **********************************************************************/
    public elementTraduc chargeUneTraduc(int id) throws Exception {
    	elementTraduc et = new elementTraduc();
    	String query = "select * from traduction where id = " + Integer.toString(id);
    	System.out.println(query);
    	ResultSet rs = statement.executeQuery( query );
    	while (rs.next()) {
    		et.setId(rs.getInt("id"));
    		et.setAnglais(rs.getString("anglais"));
    		et.setFrancais(rs.getString("francais") );
    		et.setGBOkSQLite(rs.getInt("connuGB"));
    		et.setFOkSQLite(rs.getInt("connuF"));
    		if ( (rs.getString("fichiermp3").trim().length() > 0) && (rs.getString("fichiermp3") != null))
    			et.setFichiermp3(rs.getString("fichiermp3"));
    		else
    			et.setFichiermp3("");
    	}
    	rs.close();
    	statement.close();
    	return et;
    }
    /**********************************************************************
    * Sélection de tous les id (clé primaires) des traductions 
    * par défaut triés sur la traduc
     * @throws Exception 
    **********************************************************************/
    public ArrayList<Integer> listeMots(Integer tri) throws Exception {   
    	ArrayList<Integer> liste = new ArrayList<Integer>();
    	String query = "SELECT * FROM traduction ";

    	switch (tri) { 
    	case 0: 
    		query += "";
    		break;
    	case 1: 
    		query += "order by anglais";
    		break;
    	case 2: 
    		query += "order by francais";
    		break;
    	case 3: 
    		query += "";
    		break;
    	}
    	ResultSet rs = statement.executeQuery( query );
    	while (rs.next()) {
    		liste.add(rs.getInt("id"));
    	}
    	rs.close();
    	statement.close();    	
    	return liste;
    }
    /**********************************************************************
     * 
     * @param et
     * @throws Exception
     **********************************************************************/
    public void majSon(elementTraduc et) throws Exception  {
    	String query = "";
    	query += "UPDATE traduction ";
    	query += "set fichiermp3 = '" +  et.getFichiermp3() + "' ";
    	query += "WHERE ID ='" + et.getId()  + "'";

    	System.out.println(query);
    	statement.executeUpdate(query);
    }
    /**********************************************************************
     * 
     * @param et
     * @throws Exception
     **********************************************************************/
    public void supprimeSon(elementTraduc et) throws Exception  {
    	String query = "";
    	query += "UPDATE traduction ";
    	query += "set fichiermp3 = '' ";
    	query += "WHERE ID ='" + et.getId()  + "'";

    	System.out.println(query);
    	statement.executeUpdate(query);
    	statement.close();
    }
    /**********************************************************************
     * Statistiques
     * @param res1
     * @throws Exception
     **********************************************************************/
    public void stats(int[] res1) throws Exception  {
    	String query = "SELECT Count(*) as res FROM traduction";
    	System.out.println(query);
    	ResultSet rs = statement.executeQuery( query );
    	while (rs.next()) {
    		res1[0] = rs.getInt("res");
    	}
    	query = "SELECT Count(*) as res FROM traduction where connuGB = 0";
    	System.out.println(query);
    	rs = statement.executeQuery( query );
    	while (rs.next()) {
    		res1[1] = rs.getInt("res");
    	}
    	query = "SELECT Count(*) as res FROM traduction where connuGB = 1";
    	System.out.println(query);
    	rs = statement.executeQuery( query );
    	while (rs.next()) {
    		res1[2] = rs.getInt("res");
    	}
    	query = "SELECT Count(*) as res FROM traduction where connuF = 0";
    	System.out.println(query);
    	rs = statement.executeQuery( query );
    	while (rs.next()) {
    		res1[3] = rs.getInt("res");
    	}
    	query = "SELECT Count(*) as res FROM traduction where connuF = 1";
    	System.out.println(query);
    	rs = statement.executeQuery( query );
    	while (rs.next()) {
    		res1[4] = rs.getInt("res");
    	}
    	query = "SELECT Count(*) as res FROM traduction where fichiermp3 != ''";
    	System.out.println(query);
    	rs = statement.executeQuery( query );
    	while (rs.next()) {
    		res1[5] = rs.getInt("res");
    	}
    	rs.close();
    	statement.close();
    }
	public void supprimeTraduction(int ancIndex) throws Exception {
		String query = "";
		query += "delete from traduction ";
		query += "WHERE ID = '" + ancIndex  + "'";

		System.out.println(query);
		statement.executeUpdate(query);
    	statement.close();
	}
	public ArrayList<Integer> cherche(Integer typeCh, String crit) throws Exception {
		ArrayList<Integer> liste = new ArrayList<Integer>();
		String query = "SELECT * FROM traduction ";
		if (typeCh == 0) {
			query += "where anglais like '%" + crit + "%' order by anglais";
		} else {
			query += "where francais like '%" + crit + "%' order by francais";
		}
		//System.out.println(query);
		ResultSet rs = statement.executeQuery( query );
		while (rs.next()) {
			liste.add(rs.getInt("id"));
		}
		rs.close();
    	statement.close();
		return liste;
	}
    /**********************************************************************
     * 
     * @param et
     * @throws Exception
     **********************************************************************/
    public Boolean existeDeja(String mot, Boolean GB ) throws Exception  {
    	String query = "";
    	query += "select * from traduction ";
    	if (GB) {
    		query += "where anglais = '" + mot + "'";  
    	} else {
    		query += "where francais='" + mot + "'";
    	}

    	System.out.println(query);
    	ResultSet rs = statement.executeQuery( query );
    	int i = 0;
    	while (rs.next()) {
    		i++;
    	}
    	rs.close();
    	statement.close();
    	return (i == 0);
    }
    public void destuctor() {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        System.err.println(e);
      }
    }
}
