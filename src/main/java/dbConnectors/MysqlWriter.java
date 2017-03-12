package main.java.dbConnectors;

import java.sql.*;
import java.util.UnknownFormatConversionException;

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

  public MysqlWriter() throws SQLException {
    super();
    this.getConnection();
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
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next() == true) {
        System.out.println("Property already exists in database!");
        logger.warn(String.format(
            "Did not insert zestimate because an identical zestimate already exists in the database. "
                + "\n " + "ZPID: %s, Zestimate: %s, Last Updated: %s",
            z.getZpid().toString(), z.getZestimate(), z.getLastUpdated().toString()));
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
        logger.trace("Executing query: " + checkStmt.toString());
        ps.execute();
        logger.info("Zetimate for zpid: " + z.getZpid() + " has successfully been inserted.");
      }
    } catch (SQLException e) {
      logger.error("A SQLException occured while attempting to insert Zestimate.", e);
    } catch (UnknownFormatConversionException e) {
      logger.error(
          "There was an error converting and/or formatting a String in method insertObject(Zestimate z) in MysqlWriter",
          e);
    }
  }

  public void updateZestimateValue(Zestimate z, int zestimateChange) {
    try {
      String query =
          "SELECT * FROM Zestimates WHERE zpid = ? AND zestimate = ? AND lastUpdated = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setObject(1, z.getZpid(), Types.BIGINT);
      checkStmt.setInt(2, z.getZestimate());
      checkStmt.setObject(3, z.getLastUpdated());
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next() == false) {
        logger.warn(String.format(
            "Zestimate does not exist in database! Cannot update." + "\n "
                + "ZPID: %s, Zestimate: %i, Last Updated: %s",
            z.getZpid().toString(), z.getZestimate(), z.getLastUpdated().toString()));
      } else {
        int newZest = z.getZestimate() + zestimateChange;
        int newThirty = z.getThirtyDayChange() + zestimateChange;
        int newHigh = z.getValuationHigh();
        int newLow = z.getvaluationLow();
        if (newZest > newHigh) {
          newHigh = newZest;
        } else if (newZest < newLow) {
          newLow = newZest;
        }

        String stmt =
            "UPDATE Zestimates SET zestimate = ?, thirtyDayChange = ?, valuationHigh = ?, valuationLow = ?, lastUpdated = CURDATE()"
                + " WHERE zpid = ? AND zestimate = ?";
        PreparedStatement ps = this.connect.prepareStatement(stmt);
        ps.setInt(1, newZest);
        ps.setInt(2, newThirty);
        ps.setInt(3, newHigh);
        ps.setInt(4, newLow);
        ps.setObject(5, z.getZpid(), Types.BIGINT);
        ps.setInt(6, z.getZestimate());
        logger.trace("Executing query: " + ps.toString());
        ps.execute();

        logger.info("Successfully updated Zestimate value: " + ps.toString());
      }
    } catch (SQLException e) {
      logger.error("A SQLException occured while attempting to update a Zestimate.", e);
    }

  }

  public void updateRentZestimate(Zestimate z, int rentChange) {
    try {
      String query =
          "SELECT * FROM Zestimates WHERE zpid = ? AND zestimate = ? AND lastUpdated = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setObject(1, z.getZpid(), Types.BIGINT);
      checkStmt.setInt(2, z.getZestimate());
      checkStmt.setObject(3, z.getLastUpdated());
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next() == false) {
        logger.warn(String.format(
            "Zestimate does not exist in database! Cannot update." + "\n "
                + "ZPID: %s, Zestimate: %i, Last Updated: %s",
            z.getZpid().toString(), z.getZestimate(), z.getLastUpdated().toString()));
      } else {
        int newRentZest = z.getRentZestimate() + rentChange;
        int newThirty = z.getRentThirtyDayChange() + rentChange;
        int newHigh = z.getMaxRent();
        int newLow = z.getMinRent();
        if (newRentZest > newHigh) {
          newHigh = newRentZest;
        } else if (newRentZest < newLow) {
          newLow = newRentZest;
        }

        String stmt =
            "UPDATE Zestimates SET rentZestimate = ?, rentThirtyDayChange = ?, rentZestimateLow = ?, rentZestimateHigh = ?, lastUpdated = CURDATE()"
                + " WHERE zpid = ? AND zestimate = ?";
        PreparedStatement ps = this.connect.prepareStatement(stmt);
        ps.setInt(1, newRentZest);
        ps.setInt(2, newThirty);
        ps.setInt(3, newLow);
        ps.setInt(4, newHigh);
        ps.setObject(5, z.getZpid(), Types.BIGINT);
        ps.setInt(6, z.getZestimate());
        logger.trace("Executing query: " + ps.toString());
        ps.execute();
        logger.info("Successfully updated Zestimate value: " + ps.toString());
      }
    } catch (SQLException e) {
      logger.error("A SQLException occured while attempting to update a Rent Zestimate.", e);
    }

  }

  @Override
  public void insertObject(Neighborhood n) {
    try {
      String query = "SELECT * FROM Neighborhoods WHERE regionID = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setInt(1, n.getRegionID());
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next() == true) {
        logger.warn("Neighborhood already exists in database: regionID = " + n.getRegionID());
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
        logger.trace("Executing query: " + ps.toString());
        ps.execute();
      }
    } catch (SQLException e) {
      logger.error("A SQLException occured while attempting to insert a Neighborhood.", e);
    }
  }


  @Override
  public void insertObject(Property p) {
    try {
      String query = "SELECT zpid FROM Properties WHERE zpid = ?;";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setObject(1, p.getZpid(), Types.BIGINT);
      logger.info("Executing query: " + checkStmt.toString());
      ResultSet checkResult = checkStmt.executeQuery();
      if (checkResult.next()) {
        logger.warn("Property already exists in database. ZPID = " + p.getZpid()
            + " Will not attempt to reinsert property.");
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
      logger.info("Executing query: " + preparedStmt.toString());
      preparedStmt.execute();

      logger.info("Property Inserted Successfully: ZPID: " + p.getZpid());
    } catch (SQLException e) {
      logger.error("Error occured when inserting Property: ", e);
    }
  }

  @Override
  public void insertObject(PropertyDetails pd) {
    try {
      String query = "SELECT zpid FROM PropertyDetails WHERE zpid = ?;";
      PreparedStatement checkStmt = this.connect.prepareStatement(query);
      checkStmt.setObject(1, pd.getZpid(), Types.BIGINT);
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkPD = checkStmt.executeQuery();
      if (checkPD.next() == true) {
        logger.warn("Property Details already exist in database: ZPID = " + pd.getZpid());
      } else {
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

        logger.trace("Executing query: " + ps.toString());
        ps.execute();

        logger.info("Property Details were successfully inserted for zpid: " + pd.getZpid());
      }
    } catch (SQLException e) {
      logger.error("Property Details could not be inserted for zpid: " + pd.getZpid(), e);
    }
  }

  @Override
  public void insertObject(ZillowComparable zc) {
    try {
      String check = "SELECT compID FROM Comparables" + " WHERE primaryZPID = ? AND compZPID = ?;";
      PreparedStatement checkStmt = this.connect.prepareStatement(check);
      checkStmt.setObject(1, zc.getPrimaryZpid(), Types.BIGINT);
      checkStmt.setObject(2, zc.getCompZpid(), Types.BIGINT);
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkZC = checkStmt.executeQuery();
      if (checkZC.next() == true) {
        logger.warn("Comparable already exists in database!: primaryZPID = " + zc.getPrimaryZpid()
            + " and compZPID = " + zc.getCompZpid());
      } else {
        String statement =
            "INSERT INTO Comparables(primaryZPID, compZPID, compScore, compAddress, compZipcode) VALUES "
                + "(?,?,?,?,?)";

        PreparedStatement ps = this.connect.prepareStatement(statement);

        ps.setObject(1, zc.getPrimaryZpid(), Types.BIGINT);
        ps.setObject(2, zc.getCompZpid(), Types.BIGINT);
        ps.setFloat(3, zc.getCompScore());
        ps.setString(4, zc.getCompAddress());
        ps.setInt(5, zc.getCompZip());
        logger.trace("Executing query: " + ps.toString());
        ps.execute();

        logger.info("Comparable Successfully Inserted. primaryZPID = " + zc.getPrimaryZpid()
            + " and compZPID = " + zc.getCompZpid());
      }
    } catch (SQLException e) {
      logger.error("SQLException occurred while inserting comparable: primaryZPID = "
          + zc.getPrimaryZpid() + " and compZPID = " + zc.getCompZpid(), e);
    }
  }

  public void deleteObject(ZillowComparable zc) {
    try {
      String check = "SELECT compID FROM Comparables WHERE primaryZPID = ? AND compZPID = ?;";
      PreparedStatement checkStmt = this.connect.prepareStatement(check);
      logger.trace("Executing query: " + checkStmt.toString());
      ResultSet checkZC = checkStmt.executeQuery();
      if (checkZC.next() == false) {
        logger.warn("Comparable does not exist in database! Cannot delete. primaryZPID = "
            + zc.getPrimaryZpid() + " and compZPID = " + zc.getCompZpid());
      } else {
        String statement = "DELETE FROM Comparables WHERE primaryZPID = ? AND " + "compZpid = ?";
        PreparedStatement ps = this.connect.prepareStatement(statement);

        ps.setObject(1, zc.getPrimaryZpid(), Types.BIGINT);
        ps.setObject(2, zc.getCompZpid(), Types.BIGINT);
        logger.trace("Executing query: " + ps.toString());
        ps.execute();
        logger.info("Successfully deleted comparable from Database. primaryZPID = "
            + zc.getPrimaryZpid() + " and compZPID = " + zc.getCompZpid());
      }
    } catch (SQLException e) {
      logger.error("ERROR: Could not delete comparable: primaryZPID = " + zc.getPrimaryZpid()
          + " and compZPID = " + zc.getCompZpid(), e);
    }
  }

  @Override
  public void insertObject(TaxAssessment ta) {
    try {

      String countExisting =
          "SELECT COUNT(*) FROM TaxAssessments WHERE zpid = ? AND taxYear = ? and taxAssessment = ?";
      PreparedStatement countStmt = this.connect.prepareStatement(countExisting);
      countStmt.setObject(1, ta.getZpid(), Types.BIGINT);
      countStmt.setObject(2, ta.getTaxYear());
      countStmt.setObject(3, ta.getTaxAssessment());
      ResultSet countResult = countStmt.executeQuery();

      int count = 0;
      if (countResult.next()) {
        count = countResult.getInt(1);
      }

      if (count < 1) {
        String statement =
            "INSERT INTO TaxAssessments(zpid, taxYear, taxAssessment) VALUES " + "(?,?,?)";

        PreparedStatement ps = this.connect.prepareStatement(statement);

        ps.setObject(1, ta.getZpid(), Types.BIGINT);
        ps.setObject(2, ta.getTaxYear());
        ps.setObject(3, ta.getTaxAssessment());

        logger.trace("Executing query: " + ps.toString());
        ps.execute();

        logger.info("Tax Assessment Successfully Added For ZPID: " + ta.getZpid());
      } else {
        logger.info(
            "Did not insert TaxAssessment because there is an existing identical entry: zpid = "
                + ta.getZpid() + " year = " + ta.getTaxYear() + " and ammount = "
                + ta.getTaxAssessment());
      }

    } catch (SQLException e) {
      logger.error(
          "A SQLException was thrown while attempting to insert a TaxAssessment into the database: "
              + ta.toString(),
          e);
    }
  }
}
