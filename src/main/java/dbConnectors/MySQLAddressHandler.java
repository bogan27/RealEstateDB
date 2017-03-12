/**
 * 
 */
package main.java.dbConnectors;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author brandonbogan
 *
 */
public class MySQLAddressHandler extends MySQLConnectorAbstract implements AddressHandler {

  private String selectorFilterStatement =
      "ZipGeo.MSAName LIKE \"Boston-Cambridge-Quincy, MA-NH MSA\"";

  public MySQLAddressHandler() throws SQLException {
    super();
    this.getConnection();
  }


  /*
   * (non-Javadoc)
   * 
   * @see DBSource.AddressHandler#getNextAddressToUpdate()
   */
  @Override
  public Map<String, String> getNextAddressToUpdate(int maxActiveAddresses) {

    boolean tryNewAddress = true;

    Map<String, String> response = new HashMap<String, String>();

    // First select a property that does not have a value for the boolean field api_result, which is
    // likely a new addresses in the table
    try {
      String statement =
          "SELECT COUNT(Addresses.address_id) FROM Addresses INNER JOIN ZipGeo ON Addresses.zip = ZipGeo.zip WHERE api_result = ? AND "
              + this.selectorFilterStatement;
      PreparedStatement ps = this.connect.prepareStatement(statement);
      ps.setBoolean(1, true);
      logger.trace("Now executing query: " + ps.toString());
      ResultSet results = ps.executeQuery();
      if (results.next()) {
        tryNewAddress = results.getInt(1) < maxActiveAddresses;
        logger.trace("tryNewAddress set to: " + tryNewAddress);
      }

      if (tryNewAddress) {
        statement =
            "SELECT Addresses.address, Addresses.zip FROM Addresses INNER JOIN ZipGeo ON Addresses.zip = ZipGeo.zip WHERE api_result IS NULL AND "
                + this.selectorFilterStatement + " LIMIT 1";
        ps = this.connect.prepareStatement(statement);
        logger.trace("Executing query: " + ps.toString());
        results = ps.executeQuery();
        if (results.next()) {
          for (int i = 1; i < results.getMetaData().getColumnCount() + 1; i++) {
            String columnName = results.getMetaData().getColumnName(i);
            String columnValue = results.getObject(i).toString();
            response.put(columnName, columnValue);
          }
        }
      }

      // If no results, select the address with the oldest value for last_updated, but that has an
      // api_result value of true, meaning that the last time the search API was called on this
      // address, data was returned..
      if (!tryNewAddress || response.isEmpty()) {
        logger.trace(
            "No new addresses available, so now looking for the stalest property that was successfully updated. tryNewAddress: "
                + tryNewAddress + " & response.isEmpty() = " + response.isEmpty());
        statement =
            "SELECT Addresses.address, Addresses.zip FROM Addresses INNER JOIN ZipGeo ON Addresses.zip = ZipGeo.zip WHERE api_result = ? AND "
                + this.selectorFilterStatement + " ORDER BY last_updated ASC LIMIT 1";
        ps = this.connect.prepareStatement(statement);
        ps.setBoolean(1, true);
        logger.trace("Now executing query: " + ps.toString());
        results = ps.executeQuery();
        if (results.next()) {
          for (int i = 1; i < results.getMetaData().getColumnCount() + 1; i++) {
            String columnName = results.getMetaData().getColumnName(i);
            String columnValue = results.getObject(i).toString();
            response.put(columnName, columnValue);
            logger.trace(
                "Response updated to contain property that had the oldest successfull update.");
          }
        }
      }

      // If still no results, select the oldest address that was not successfully updated last time
      if (response.isEmpty()) {
        statement =
            "SELECT Addresses.address, Addresses.zip FROM Addresses INNER JOIN ZipGeo ON Addresses.zip = ZipGeo.zip WHERE api_result = ? AND "
                + this.selectorFilterStatement + " ORDER BY last_updated ASC LIMIT 1";
        ps = this.connect.prepareStatement(statement);
        ps.setBoolean(1, false);
        results = ps.executeQuery();
        if (results.next()) {
          for (int i = 1; i < results.getMetaData().getColumnCount(); i++) {
            String columnName = results.getMetaData().getColumnName(i);
            String columnValue = results.getObject(i).toString();
            response.put(columnName, columnValue);
            logger.trace(
                "Response updated to contain the oldest property that was not successfully updated.");
          }
        }
      }

    } catch (SQLException e) {
      logger.error("A SQLException occurred while trying to query addresses in the database.", e);
    }
    return response;
  }

  /*
   * (non-Javadoc)
   * 
   * @see DBSource.AddressHandler#processAddress(java.util.Map)
   */
  @Override
  public void processAddress(Map<String, String> values) {
    logger.info("Attempting to add a new Address to the DB.");
    String address = null;
    int zip = 0;
    boolean api_result = false;
    BigInteger zpid = new BigInteger("0");
    if (values.containsKey("address")) {
      address = values.get("address");
      logger.trace("Address: " + address);
    } else {
      logger.warn("Argument for values does contain the required key-value pair for 'address'");
      throw new IllegalArgumentException(
          "Argument for values does contain the required key-value pair for 'address'");
    }
    if (values.containsKey("zip")) {
      zip = Integer.parseInt(values.get("zip"));
      logger.trace("Zip: " + zip);
    } else {
      logger.warn("Argument for values does contain the required key-value pair for 'zip'");
      throw new IllegalArgumentException(
          "Argument for values does contain the required key-value pair for 'zip'");
    }
    if (values.containsKey("api_result")) {
      api_result = Boolean.parseBoolean(values.get("api_result"));
      logger.trace("API Result for address: " + api_result);
    } else {
      logger.warn("Argument for values does contain the required key-value pair for 'api_result'");
      throw new IllegalArgumentException(
          "Argument for values does contain the required key-value pair for 'api_result'");
    }
    if (values.containsKey("zpid")) {
      zpid = new BigInteger(values.get("zpid"));
      logger.trace("ZPID: " + zpid);
    }
    
    

    PreparedStatement ps;
    try {
      this.deleteAddress(address, zip);
//      String selection = "SELECT COUNT(*) FROM Addresses WHERE address = ? and zip = ?";
//      PreparedStatement selectStmt = this.connect.prepareStatement(selection);
//      selectStmt.setString(1, address);
//      selectStmt.setInt(2, zip);
//      logger.info("Now executing query: " + selectStmt.toString());
//      ResultSet resultCount = selectStmt.executeQuery();
//      int count = 0;
//      if (resultCount.next()) {
//        count = resultCount.getInt(1);
//      } else {
//        throw new RuntimeException("Something's wrong with the address information provided");
//      }
//
//      if (count <= 0) {
//        logger.info("No matching address found in the table. Preparing to insert a new Address.");
        String query =
            "INSERT INTO Addresses (address, zip, api_result, Properties_zpid) VALUES (?,?,?,?)";
        ps = this.connect.prepareStatement(query);
        ps.setString(1, address);
        ps.setInt(2, zip);
        ps.setBoolean(3, api_result);
        ps.setObject(4, zpid, Types.BIGINT);
        logger.info("Now executing query: " + ps.toString());
        ps.execute();
//      } else {
//        // Clean up any duplicate address entries
//        this.deleteAddress(address, zip);
//
//        String update =
//            "UPDATE Addresses SET api_result = ?, last_updated = CURRENT_TIMESTAMP WHERE address = ? and zip  = ? LIMIT 1";
//        PreparedStatement updateStmt = this.connect.prepareStatement(update);
//        updateStmt.setBoolean(1, api_result);
//        updateStmt.setString(2, address);
//        updateStmt.setInt(3, zip);
//        logger.trace("Now executing query: " + updateStmt.toString());
//        updateStmt.execute();
//      }
    } catch (SQLException e) {
      logger.error("A SQLExecption occurred while trying to process the address. Address = "
          + address + "; Zipcode = " + zip + "; api_result = " + api_result + ".", e);
    }
  }

  /**
   * Deletes all records from the Addresses table that have the given address and zipcode (this
   * helps keep the Addresses table deduplicated)
   * 
   * @param address The street address to delete
   * @param zip
   * @throws SQLException
   */
  private void deleteAddress(String address, int zip) throws SQLException {
    String delete = "DELETE FROM Addresses WHERE address LIKE ? and zip = ?";
    PreparedStatement deleteStmt = this.connect.prepareStatement(delete);
    deleteStmt.setString(1, address);
    deleteStmt.setInt(2, zip);
    logger.info("Now executing query: " + deleteStmt.toString());
    deleteStmt.execute();
  }


  @Override
  public int getPropertyCount() {
    String select = "SELECT COUNT(*) FROM Addresses";
    PreparedStatement selectStmt;
    int count = 0;
    try {
      selectStmt = this.connect.prepareStatement(select);
      logger.trace("Now executing query: " + selectStmt);
      ResultSet rs = selectStmt.executeQuery();
      if (rs.next()) {
        count = rs.getInt(1);
      } else {
        logger.warn("Empty result set returned from getting a count of the addresses.");
        throw new RuntimeException("Could not get count of addresses");
      }
    } catch (SQLException e) {
      logger.error(
          "A SQLExecption occurred while trying to get a count of addresses in the database.", e);
    }
    return count;
  }

}
