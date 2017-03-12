package main.java.dbConnectors;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.classes_for_db.Property;


public class BostonMSAValidator extends MySQLConnectorAbstract implements Validator {

  private Property property;
  private String msaName = "Boston";

  public BostonMSAValidator() {
    try {
      this.getConnection();
    } catch (SQLException e) {
      logger.error("Could not connect to Database when instantiating class: " + this.getClass().getName(), e);
    }
  }

  public Property getProperty() {
    return this.property;
  }

  public void setProperty(Property p) {
    this.property = p;
  }

  public boolean validate(Property p) {
    logger.trace("Now validating property with ZPID: " + p.getZpid().toString());
    return this.validateZip(p.getZipCode());// && this.validateComp(p);
  }

  // make sure property falls within the right geography
  @Override
  public boolean validateZip(int zipcode) {
    boolean response = false;
    try {
      String query =
          "SELECT MSAName FROM ZipGeo WHERE zip = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      checkStmt.setObject(1, zipcode);
      logger.trace("Now executing query: " + checkStmt.toString());
      ResultSet rs = checkStmt.executeQuery();
      if(rs.next()){
        String MSA = (rs.getString(1));
        response = MSA.toLowerCase().contains(this.msaName.toLowerCase());
      }
      else {
        logger.warn("MSA not found for this zip code: " + zipcode + ". Property will not be validated.");
      }     	
    }
    catch (SQLException e) {
      logger.error("Could not validate property with geography - a SQLException occurred when querying database.", e);
    }
    logger.info("Property " + zipcode + " validated geographically for geography \"" + this.msaName + "\": " + response);
    return response;
  }

  // make sure property is not primary for any comparables
  public boolean validateComp(Property p) {
    boolean response = false;
    try {
      String query =
          "SELECT * FROM Comparables WHERE primaryZPID = ?";
      PreparedStatement checkStmt = this.connect.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      checkStmt.setObject(1, p.getZpid());
      logger.trace("Now executing query: " + checkStmt.toString());
      ResultSet rs = checkStmt.executeQuery();
      response =  rs.next();
    }
    catch (SQLException e) {
      logger.error("ERROR: Could not validate property with comparables because a SQLExecption occured when querying DB.", e);
    }
    logger.info("Property " + p.getZpid() + " validated in Comparable table: " + response);
    return response;
  }
}