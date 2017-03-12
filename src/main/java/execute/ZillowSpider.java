/**
 * 
 */
package main.java.execute;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.ZillowComparable;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.MySQLAddressHandler;
import main.java.dbConnectors.MysqlWriter;
import main.java.dbConnectors.Validator;
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
  private Logger logger;

  public ZillowSpider() throws SQLException {
    this.callsMade = 0;
    this.addressHandler = new MySQLAddressHandler();
    this.dbwriter = new MysqlWriter();
    this.logger = LogManager.getLogger(this.getClass().getName());
  }

  public boolean spider(Queue<String> toDoList, int callLimit, int maxCompsPerProperty,
      Validator validator) {
    System.out.println("Now Spidering...");
    System.out.println("To Do List Size: " + toDoList.size());
    System.out.println("Call Limit: " + callLimit);
    while (!toDoList.isEmpty() && callsMade < callLimit) {
      String zpid = toDoList.poll();
      System.out.println("Looking up zpid: " + zpid);
      if (zpid == null || zpid.isEmpty()) {
        break;
      }
      List<String> newComps = this.processProperty(zpid, maxCompsPerProperty, validator);
      System.out.println("Comparables found: " + newComps.size());
      toDoList.addAll(newComps);
      System.out.println("Finished processing ZPID: " + zpid);
    }
    return true;
  }

  private List<String> processProperty(String zpid, Integer maxCompsPerProperty,
      Validator validator) {
    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpid);
    params.put("count", maxCompsPerProperty.toString());

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepComps).addParams(params).build();
    System.out.println("Request URL: " + request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    List<String> resultingZPIDs = new ArrayList<String>();
    try {
      String data = apiTool.makeApiCall(request);
      System.out.println(data.substring(0, 50));
      this.callsMade++;

      // Parse the data into a list of properties
      // TODO This needs to be changed to a GetDeepCompsResultParser
      GetDeepCompsResultParser parser = new GetDeepCompsResultParser(data);
      parser.setReturnComps(false);
      parser.setReturnProperties(true);
      // SearchResultParser parser = new SearchResultParser(data);
      List<DbTableObject> searchResults = parser.parseData();
      logger.trace("Number of DBTableObjects returned in the result set: " + searchResults.size());
      int propertyCount = 0;
      for (DbTableObject dbto : searchResults) {
        if (dbto instanceof Property) {
          propertyCount++;
        }
      }
      System.out.println("Number of Properties resturned in the result set: " + propertyCount);
      for (DbTableObject dbto : searchResults) {
        // TODO Upgrade GetDeepComps parser to be able to return Comparables or Properties
        Property p = (Property) dbto;
        if (validator.validate(p)) {
          String nextZpid = p.getZpid().toString();
          List<ZillowComparable> comps = p.getZillowComps();
          System.out.println("Additional comps: " + comps.size());
          for (ZillowComparable c : comps) {
            resultingZPIDs.add(c.getCompZpid().toString());
          }
          this.processComparableAddresses(comps);
          this.processPropertyDetails(zpid);
          p.writeToDB(this.dbwriter);
        }
      }
    } catch (IOException e) {
      logger.error(
          "IOException occurred while making the api call for a property. \nURL: " + request, e);
    } catch (SAXException e) {
      logger.error("SAXException occurred while parsing the results of the call to " + request, e);
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
    System.out.println("URL for Property Details: " + request);

    // Make the actual API call
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    try {
      String data = apiTool.makeApiCall(request);
      this.callsMade++;

      PropertyDetailParser parser = new PropertyDetailParser(data);
      List<DbTableObject> detailsList = parser.parseData();
      for (DbTableObject dbto : detailsList) {
        dbto.writeToDB(this.dbwriter);
      }
    } catch (IOException e) {
      logger.error(
          "IOException occurred while making the api call for a property's details. \nURL: " + request, e);
    } catch (SAXException e) {
      logger.error("SAXException occurred while parsing the results of the call to " + request, e);
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
      compAddressValues.put("zpid", c.getCompZpid().toString());
      this.addressHandler.processAddress(compAddressValues);
    }
    return true;
  }
}
