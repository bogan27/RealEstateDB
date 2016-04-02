package DBSource;
import java.util.*;
import java.util.Date;

import classes_for_db.Zestimate;

import java.math.BigInteger;
import java.sql.*;

public class DBWriter {
	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "mysqlUser";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "zillow";

	/** The name of the computer running MySQL */
	private final String serverName = "54.86.82.1";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;
	
	/** the DB name */
	private final String dbName = "RealEstate";
	
	private Connection connect;
	
	public DBWriter() {
		try {
			this.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public Connection getConnection() throws SQLException {
		Scanner sc = new Scanner(System.in);
		Connection conn = null;
		Properties connectionProps = new Properties();
//		System.out.println("Enter your username:");
//		String enteredUsername = sc.nextLine();
//		System.out.println("Enter the password:");
//		String enteredPassword = sc.nextLine();
//		if(!enteredUsername.equals(this.userName)) {
//			System.out.println("Sorry! Wrong username. Try again.");
//			getConnection();
//			return conn;
//		}
//		else if(!enteredPassword.equals(this.password)) {
//			System.out.println("Sorry! Wrong password. Try again.");
//			getConnection();
//			return conn;
//		}
//		else {
			connectionProps.put("user", this.userName);
			connectionProps.put("password", this.password);
	
			conn = DriverManager.getConnection("jdbc:mysql://"
					+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
					connectionProps);
			connect = conn;
		
		return conn;
	}
	
	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	public void run() {

		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			if(conn == null) {
				System.out.println("Could not connect to database");
			}
			else {
				//System.out.println("Connected to database");
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
	}
	
	public void insertZestimate(Zestimate z) {
/*
 *  sb.append("ZPID: " + this.getZpid() + "\n");
    sb.append("Zestimate: " + this.getZestimate() + "\n");
    sb.append("Last updated: " + this.getLastUpdatedString() + "\n");
    sb.append("High Valuation: " + this.getValuationHigh() + "\n");
    sb.append("Low valuation: " + this.getvaluationLow() + "\n");
    sb.append("30 day change: " + this.getThirtyDayChange() + "\n");
    sb.append("Percentile Value: " + this.getPercentileValue() + "\n");
 */
		try {
		String statement = 
        "INSERT INTO Zestimates(zpid, zestimate, lastUpdated, thirtyDayChange, valuationHigh, " +
        "valuationLow, percentileValue) VALUES (" +
		z.getZpid() + ", " + z.getZestimate() + ", " +
		z.getLastUpdatedString() + ", " + z.getValuationHigh() + ", " +
		z.getvaluationLow() + ", " + z.getThirtyDayChange() + ", " +
		z.getPercentileValue() + ");";	
		
		this.executeUpdate(connect, statement);
		
		}
		catch (SQLException e) {
		System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}
	}
	
	
	/**
	 * Connect to the DB and do some stuff
	 */
	public static void main(String[] args) {
		DBWriter app = new DBWriter();
		app.run();
	}
	
	
	
}