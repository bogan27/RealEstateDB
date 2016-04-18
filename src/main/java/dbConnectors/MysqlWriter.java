package main.java.dbConnectors;

import java.sql.*;

import main.java.classes_for_db.Neighborhood;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.PropertyDetails;
import main.java.classes_for_db.TaxAssessment;
import main.java.classes_for_db.Zestimate;
import main.java.classes_for_db.ZillowComparable;

/**
 * @author ronfarizon
 */

public class MysqlWriter extends MySQLConnectorAbstract implements DBWriter {

  public MysqlWriter() {
    super();
    try {
      this.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void insertObject(Zestimate z) {

    try {
      String query =
          "SELECT * FROM Zestimates WHERE zpid = ? AND zestimate = ? AND lastUpdated = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setObject(1, z.getZpid(), Types.BIGINT);
      checkStmt.setInt(2, z.getZestimate());
      checkStmt.setObject(3, z.getLastUpdated());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next() == true) {
        System.out.println("Property already exists in database!");
        return;
      } else {
        String statement =
            "INSERT INTO Zestimates(zpid, zestimate, lastUpdated, thirtyDayChange, valuationHigh, "
                + "valuationLow, percentileValue, rentZestimate, rentThirtyDayChange, "
                + "rentZestimateLow, rentZestimateHigh) VALUES " + "(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = this.connect.prepareStatement(statement);
        ps.setObject(1, z.getZpid(), Types.BIGINT);
        ps.setInt(2, z.getZestimate());
        ps.setObject(3, z.getLastUpdated());
        ps.setInt(4, z.getThirtyDayChange());
        ps.setInt(5, z.getValuationHigh());
        ps.setInt(6, z.getvaluationLow());
        ps.setFloat(7, z.getPercentileValue());
        ps.setInt(8, z.getRentZestimate());
        ps.setInt(9, z.getRentThirtyDayChange());
        ps.setInt(10, z.getMinRent());
        ps.setInt(11, z.getMaxRent());
        
        ps.execute();

        System.out
            .println("Zetimate for zpid: " + z.getZpid() + " has successfully been inserted.");
      }
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  @Override
  public void insertObject(Neighborhood n) {
    try {
      String query = "SELECT * FROM Neighborhoods WHERE regionID = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setInt(1, n.getRegionID());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next() == true) {
        System.out.println("Property already exists in database!");
        return;
      } else {

        String statement =
            "INSERT INTO Neighborhoods(regionID, name, zipcode, zindex, zIndexChange, type)"
                + " VALUES " + "(?,?,?,?,?,?)";

        PreparedStatement ps = this.connect.prepareStatement(statement);

        ps.setInt(1, n.getRegionID());
        ps.setString(2, n.getName());
        ps.setInt(3, n.getZipCode());
        ps.setInt(4, n.getzIndex());
        ps.setFloat(5, n.getzIndexChange());
        ps.setString(6, n.getType());

        ps.execute();
      }
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
    }
  }


  @Override
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
      System.out.println("Year built: " + p.getYearBuilt());
      preparedStmt.execute();

      System.out.println(statement);
      System.out.println("\n" + "Record Inserted Successfully!" + "\n");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  @Override
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
          "INSERT INTO PropertyDetails(zpid, status, posting_type, last_updated_date, year_updated,"
              + " number_floors, basement, roof_type, parking_type, rooms, home_description, neighborhood_name, "
              + "school_district, page_views_this_month, page_views_total) VALUES "
              + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

      PreparedStatement ps = this.connect.prepareStatement(statement);

      ps.setObject(1, pd.getZpid(), Types.BIGINT);
      ps.setString(2, pd.getStatus());
      ps.setString(3, pd.getPosting_type());
      ps.setObject(4, pd.getLastUpdated());
      ps.setObject(5, pd.getyearUpdated());
      ps.setInt(6, pd.getNumFloors());
      ps.setObject(7, pd.getBasement());
      ps.setObject(8, pd.getRoofType());
      ps.setObject(9, pd.getParkingType());
      ps.setInt(10, pd.getNumRooms());
      ps.setString(11, pd.getHomeDescription());
      ps.setString(12, pd.getNeighborhoodName());
      ps.setString(13, pd.getSchoolDistrict());
      ps.setInt(14, pd.getPageViewThisMonth());
      ps.setInt(15, pd.getPageViewsTotal());

      ps.execute();

      System.out.println("Property Details were successfully inserted for zpid: " + pd.getZpid());

    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  @Override
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

      String statement =
          "INSERT INTO Comparables(primaryZPID, compZPID, compScore, compAddress, compZipcode) VALUES "
              + "(?,?,?,?,?)";

      PreparedStatement ps = this.connect.prepareStatement(statement);

      ps.setObject(1, zc.getPrimaryZpid(), Types.BIGINT);
      ps.setObject(2, zc.getCompZpid(), Types.BIGINT);
      ps.setFloat(3, zc.getCompScore());
      ps.setString(4, zc.getCompAddress());
      ps.setInt(5, zc.getCompZip());

      ps.execute();

      System.out.println("Comparable Successfully Inserted.");
    }

    catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }
  }

  @Override
  public void insertObject(TaxAssessment ta) {
    try {
      String statement =
          "INSERT INTO TaxAssessments(zpid, taxYear, taxAssessment) VALUES " + "(?,?,?)";

      PreparedStatement ps = this.connect.prepareStatement(statement);

      ps.setObject(1, ta.getZpid(), Types.BIGINT);
      ps.setObject(2, ta.getTaxYear());
      ps.setObject(3, ta.getTaxAssessment());

      ps.execute();

      System.out.println("Tax Assessment Successfully Added For ZPID: " + ta.getZpid());

    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert");
      e.printStackTrace();
      return;
    }

  }
}
