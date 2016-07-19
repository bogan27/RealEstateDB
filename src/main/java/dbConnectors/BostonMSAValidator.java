package main.java.dbConnectors;

import main.java.classes_for_db.Property;

import java.math.BigInteger;
import java.sql.*;


public class BostonMSAValidator extends MySQLConnectorAbstract implements Validator {

	private Property property;

	public BostonMSAValidator() {
		try {
			this.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Property getProperty() {
		return this.property;
	}

	public void setProperty(Property p) {
		this.property = p;
	}

	// make sure property falls within the right geography
	public boolean validateGeo(Property p, String geo) {
		try {
			String query =
					"SELECT MSAName FROM ZipGeo WHERE zip = ?";
			PreparedStatement checkStmt = this.connect.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			checkStmt.setObject(1, p.getZipCode());
			
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next() == false) {
				System.out.println("MSA not found for this zip code!");
				return false;
			} else {
				rs.beforeFirst();
				rs.next();
				String MSA = (rs.getString(1));
				return MSA.toLowerCase().contains(geo.toLowerCase());
			}      	
		}
	    catch (SQLException e) {
	        System.out.println("ERROR: Could not validate property with geography.");
	        e.printStackTrace();
	        return false;
	      }
	}

	// make sure property is not primary for any comparables
	public boolean validateComp(Property p) {
		try {
			String query =
					"SELECT * FROM Comparables WHERE primaryZPID = ?";
			PreparedStatement checkStmt = this.connect.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			checkStmt.setObject(1, p.getZpid());
			
			ResultSet rs = checkStmt.executeQuery();
			return rs.next();
		}
	    catch (SQLException e) {
	        System.out.println("ERROR: Could not validate property with comparables.");
	        e.printStackTrace();
	        return false;
	      }
	}
}