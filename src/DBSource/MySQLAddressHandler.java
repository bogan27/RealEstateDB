/**
 * 
 */
package DBSource;

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
  
  public MySQLAddressHandler() throws SQLException{
    super();
    this.getConnection();
  }

  
  @Override
  public Map<String, String> getNextAddressToUpdate() {
    Property targetProp = null;
    Map<String, String> response = new HashMap<String, String>();
    try {
      String statement = "SELECT * FROM Addresses WHERE last_updated = ? LIMIT 1";
      PreparedStatement ps = this.connect.prepareStatement(statement);
      ps.setObject(1, null, Types.DATE);
      ResultSet results = ps.executeQuery();
      if(results.next()){
        for(int i=1; i < results.getMetaData().getColumnCount(); i++){
          String columnName = results.getMetaData().getColumnName(i);
          String columnValue = results.getObject(i).toString();
          response.put(columnName, columnValue);
        }
      }
      
      if(response.isEmpty()){
        statement = "SELECT * FROM Addresses ORDER BY last_updated LIMIT 1";
        ps = this.connect.prepareStatement(statement);
        results = ps.executeQuery();
        if(results.next()){
          for(int i=1; i < results.getMetaData().getColumnCount(); i++){
            String columnName = results.getMetaData().getColumnName(i);
            String columnValue = results.getObject(i).toString();
            response.put(columnName, columnValue);
          }
        }
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processAddress(Map<String, String> values) {
    // TODO Auto-generated method stub
    
  }

}
