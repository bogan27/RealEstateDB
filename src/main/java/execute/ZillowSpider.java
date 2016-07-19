/**
 * 
 */
package main.java.execute;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import javax.xml.bind.Validator;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.ZillowComparable;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.MySQLAddressHandler;
import main.java.dbConnectors.MysqlWriter;
import main.java.xml_parsers.GetDeepCompsResultParser;
import main.java.xml_parsers.PropertyDetailParser;
import main.java.xml_parsers.SearchResultParser;

/**
 * @author brandonbogan
 *
 */
public class ZillowSpider {

  private int callsMade;
  private MySQLAddressHandler addressHandler;
  private DBWriter dbwriter;

  public ZillowSpider() throws SQLException {
    this.callsMade = 0;
    this.addressHandler = new MySQLAddressHandler();
    this.dbwriter = new MysqlWriter();
  }

  public boolean spider(Queue<String> toDoList, int callLimit, int maxCompsPerProperty,
      Validator validator) {
    while (!toDoList.isEmpty() && callsMade < callLimit) {
      String zpid = toDoList.poll();
      if (zpid == null || zpid.isEmpty()) {
        break;
      }
      List<String> newComps = this.processProperty(zpid, maxCompsPerProperty, validator);
      toDoList.addAll(newComps);
    }
    return true;
  }

  private List<String> processProperty(String zpid, Integer maxCompsPerProperty, Validator validator) {
    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpid);
    params.put("count", maxCompsPerProperty.toString());

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepComps).addParams(params).build();

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    this.callsMade++;

    // Parse the data into a list of properties
    SearchResultParser parser = new SearchResultParser(data);
    ArrayList<DbTableObject> searchResults = parser.parseData();

    List<String> resultingZPIDs = new ArrayList<String>();
    for (DbTableObject dbto : searchResults) {
      Property p = (Property) dbto;
      if(validator.validateProperty(p)){
        String nextZpid = p.getZpid().toString();
        List<ZillowComparable> comps = p.getZillowComps();
        for(ZillowComparable c : comps){
          resultingZPIDs.add(c.getCompZpid().toString());
        }
        this.processComparableAddresses(comps);
        this.processPropertyDetails(zpid);
        p.writeToDB(this.dbwriter);
      }
    }
    return resultingZPIDs;
  }

  /**
   * Gets the property details for the property with the given zpid and writes the details to the
   * database
   * 
   * @param zpid The {@code zpid} of the property to search on
   */
  private void processPropertyDetails(String zpid) {
    String zpidString = zpid.toString();

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpidString);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request =
        builder.setBaseUrl(ZillowAPI.GetUpdatedProperyDetails).addParams(params).build();

    // Make the actual API call
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    this.callsMade++;

    PropertyDetailParser parser = new PropertyDetailParser(data);
    List<DbTableObject> detailsList = parser.parseData();
    for (DbTableObject dbto : detailsList) {
      dbto.writeToDB(this.dbwriter);
    }

  }

  private boolean processComparableAddresses(List<ZillowComparable> comparables) {
    for (ZillowComparable c : comparables) {
      c.writeToDB(this.dbwriter);
      String compAddress = c.getCompAddress();
      String zip = String.valueOf(c.getCompZip());
      boolean api_result = false;
      if (compAddress.length() > 1 && zip.length() > 1) {
        api_result = true;
      }
      HashMap<String, String> compAddressValues = new HashMap<String, String>();
      compAddressValues.put("address", compAddress);
      compAddressValues.put("zip", zip);
      compAddressValues.put("api_result", String.valueOf(api_result));
      this.addressHandler.processAddress(compAddressValues);
    }
    return true;
  }
}
