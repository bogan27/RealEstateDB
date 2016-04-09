package DBSource;

import java.util.*;
import java.util.Date;

import classes_for_db.Neighborhood;
import classes_for_db.Property;
import classes_for_db.PropertyDetails;
import classes_for_db.TaxAssessment;
import classes_for_db.Zestimate;
import classes_for_db.ZillowComparable;

import java.math.BigInteger;
import java.sql.*;

/**
 * @author ronfarizon
 */

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

    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);
    String connectionString =
        "jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName;
    System.out.println(connectionString);
    conn = DriverManager.getConnection(connectionString, connectionProps);
    connect = conn;

    return conn;
  }

  /**
   * Run a SQL command which does not return a recordset: CREATE/INSERT/UPDATE/DELETE/DROP/etc.
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
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  public void insertObject(Zestimate z) {

    try {
      String statement =
          "INSERT INTO Zestimates(zpid, zestimate, lastUpdated, thirtyDayChange, valuationHigh, "
              + "valuationLow, percentileValue) VALUES " + "(?,?,?,?,?,?,?)";
      PreparedStatement ps = this.connect.prepareStatement(statement);
      ps.setObject(1, z.getZpid(), Types.BIGINT);
      ps.setInt(2, z.getZestimate());
      ps.setObject(3, z.getLastUpdated());
      ps.setInt(4, x);
      
//      + z.getZpid() + ", " + z.getZestimate()
//              + ", " + z.getLastUpdatedString() + ", " + z.getValuationHigh() + ", "
//              + z.getvaluationLow() + ", " + z.getThirtyDayChange() + ", " + z.getPercentileValue()
//              + ");";
//
//      this.executeUpdate(connect, statement);

    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  public void insertObject(Neighborhood n) {
    try {
      String statement =
          "INSERT INTO Neighborhoods(regionID, name, zipcode, zindex, zIndexChange, type)"
              + " VALUES (" + ", " + n.getRegionID() + ", " + n.getName() + ", " + n.getZipCode()
              + ", " + n.getzIndexChange() + ", " + n.getType() + ");";

      this.executeUpdate(connect, statement);
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  public void insertObject(Property p) {
    ResultSet checkResult = null;
    try {

      String check = "SELECT zpid FROM Properties WHERE zpid = " + p.getZpid() + ";";
      checkResult = connect.createStatement().executeQuery(check);
      if (checkResult.next() == true) {
        System.out.println("Property already exists in database!");
        return;
      }
      String statement =
          "INSERT INTO Properties(zpid, streetAddress, zipcode, city, state, latitude, "
              + "longitude, regionID, countyCode, useCode, yearBuilt, lotSizeSqFt, finishedSqFt, "
              + "bathroomCount, bedroomCount, lastSoldDate, lastSoldPrice) VALUES "
              + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

      PreparedStatement preparedStmt = this.connect.prepareStatement(statement);

      preparedStmt.setObject(1, p.getZpid(), Types.BIGINT);
      preparedStmt.setString(2, p.getStreetAddress());
      preparedStmt.setInt(3, p.getZipCode());
      preparedStmt.setString(4, p.getCity());
      preparedStmt.setString(5, p.getState());
      preparedStmt.setFloat(6, p.getLatitude());
      preparedStmt.setFloat(7, p.getLongitude());
      preparedStmt.setInt(8, p.getRegionID());
      preparedStmt.setInt(9, p.getCountyCode());
      preparedStmt.setString(10, p.getUseCode());
      preparedStmt.setInt(11, p.getYearBuilt());
      preparedStmt.setInt(12, p.getLotSizeSqFt());
      preparedStmt.setInt(13, p.getFinishedSqFt());
      preparedStmt.setFloat(14, p.getBathroomCount());
      preparedStmt.setInt(15, p.getBedroomCount());
      preparedStmt.setObject(16, p.getLastSoldDate());
      preparedStmt.setInt(17, p.getLastSoldPrice());

      preparedStmt.execute();

      System.out.println(statement);
      System.out.println("\n" + "Record Inserted Successfully!" + "\n");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  public void insertObject(PropertyDetails pd) {
    ResultSet checkPD = null;
    try {
      String check = "SELECT zpid FROM PropertyDetails WHERE zpid = " + pd.getZpid() + ";";
      checkPD = connect.createStatement().executeQuery(check);
      if (checkPD.next() == true) {
        System.out.println("Property Details already exist in database!");
        return;
      }

      String statement =
          "INSERT INTO PropertDetails(zpid, status, posting_type, last_updated_date, year_updated,"
              + " number_floors, basement, roof_type, parking_type, rooms, home_description, neighborhood_name, "
              + "school_district, page_views_this_month, page_views_total) VALUES(" + pd.getZpid()
              + ", " + pd.getStatus() + ", " + pd.getPosting_type() + ", "
              + pd.getLastUpdatedString() + ", " + pd.getyearUpdated() + ", " + pd.getNumFloors()
              + ", " + pd.getBasement() + ", " + pd.getRoofType() + ", " + pd.getParkingType()
              + ", " + pd.getRooms() + ", " + pd.getRooms() + ", " + pd.getHomeDescription() + ", "
              + pd.getNeighborhoodName() + ", " + pd.getSchoolDistrict() + ", "
              + pd.getPageViewThisMonth() + ", " + pd.getPageViewsTotal() + ");";

      this.executeUpdate(connect, statement);

    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  public void insertObject(ZillowComparable zc) {
    ResultSet checkZC = null;
    try {
      String check = "SELECT compID FROM Comparables" + " WHERE primaryZPID = "
          + zc.getPrimaryZpid() + " AND compZPID = " + zc.getCompZpid() + ";";
      checkZC = connect.createStatement().executeQuery(check);
      if (checkZC.next() == true) {
        System.out.println("Comparable already exists in database!");
        return;
      }

      String statement = "INSERT INTO Comparables(primaryZPID, compZPID, compScore) VALUES("
          + zc.getPrimaryZpid() + ", " + zc.getCompZpid() + ", " + zc.getCompScore() + ");";

      this.executeUpdate(connect, statement);
    }

    catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  public void insertObject(TaxAssessment ta) {
    try {
      String statement = "INSERT INTO TaxAssessments(zpid, taxYear, taxAssessment) VALUES("
          + ta.getZpid() + ", " + ta.getTaxYear() + ", " + ta.getTaxAssessment() + "):";

      this.executeUpdate(connect, statement);
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }

  }



  // /**
  // * Connect to the DB and do some stuff
  // */
  // public static void main(String[] args) {
  // DBWriter app = new DBWriter();
  // app.run();
  // }
}
