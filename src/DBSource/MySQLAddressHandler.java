/**
 * 
 */
package DBSource;

import java.lang.annotation.Inherited;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import classes_for_db.Property;



/**
 * @author brandonbogan
 *
 */
public class MySQLAddressHandler extends MySQLConnectorAbstract implements AddressHandler {

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
  public Map<String, String> getNextAddressToUpdate() {

    Map<String, String> response = new HashMap<String, String>();

    // First select a property that does not have a value for the boolean field api_result, which is
    // likely a new addresses in the table
    try {
      String statement = "SELECT address, zip FROM Addresses WHERE api_result IS NULL LIMIT 1";
      PreparedStatement ps = this.connect.prepareStatement(statement);
      ResultSet results = ps.executeQuery();
      if (results.next()) {
        for (int i = 1; i < results.getMetaData().getColumnCount() + 1; i++) {
          String columnName = results.getMetaData().getColumnName(i);
          String columnValue = results.getObject(i).toString();
          response.put(columnName, columnValue);
        }
      }

      // If no results, select the address with the oldest value for last_updated, but that has an
      // api_result value of true, meaning that the last time the search API was called on this
      // address, data was returned..
      if (response.isEmpty()) {
        statement =
            "SELECT address, zip FROM Addresses WHERE api_result = ? ORDER BY last_updated ASC LIMIT 1";
        ps = this.connect.prepareStatement(statement);
        ps.setBoolean(1, true);
        results = ps.executeQuery();
        if (results.next()) {
          for (int i = 1; i < results.getMetaData().getColumnCount() + 1; i++) {
            String columnName = results.getMetaData().getColumnName(i);
            String columnValue = results.getObject(i).toString();
            response.put(columnName, columnValue);
          }
        }
      }

      // If still no results, select the oldest address that was not successfully updated last time
      if (response.isEmpty()) {
        statement =
            "SELECT address, zip FROM Addresses WHERE api_result = ? ORDER BY last_updated ASC LIMIT 1";
        ps = this.connect.prepareStatement(statement);
        ps.setBoolean(1, false);
        results = ps.executeQuery();
        if (results.next()) {
          for (int i = 1; i < results.getMetaData().getColumnCount(); i++) {
            String columnName = results.getMetaData().getColumnName(i);
            String columnValue = results.getObject(i).toString();
            response.put(columnName, columnValue);
          }
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
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
    String address = null;
    int zipcode = 0;
    boolean api_result = false;
    if (values.containsKey("address")) {
      address = values.get("address");
    } else {
      throw new IllegalArgumentException(
          "Argument for values does contain the required key-value pair for 'address'");
    }
    if (values.containsKey("zipcode")) {
      zipcode = Integer.parseInt(values.get("zipcode"));
    } else {
      throw new IllegalArgumentException(
          "Argument for values does contain the required key-value pair for 'zipcode'");
    }
    if (values.containsKey("api_result")) {
      api_result = Boolean.parseBoolean(values.get("api_result"));
    } else {
      throw new IllegalArgumentException(
          "Argument for values does contain the required key-value pair for 'api_result'");
    }

    PreparedStatement ps;
    try {
      String selection = "SELECT COUNT(*) FROM Addresses WHERE address = ? and zip = ?";
      

      String query = "INSERT INTO Addresses (address, zip, api_result) VALUES (?,?,?)";
      ps = this.connect.prepareStatement(query);
      ps.setString(1, address);
      ps.setInt(2, zipcode);
      ps.setBoolean(3, api_result);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
