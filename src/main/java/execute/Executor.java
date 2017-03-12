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
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.ZillowComparable;
import main.java.dbConnectors.AddressHandler;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.Validator;
import main.java.xml_parsers.GetDeepCompsResultParser;
import main.java.xml_parsers.PropertyDetailParser;
import main.java.xml_parsers.SearchResultParser;

/**
 * @author brandonbogan
 *
 */
public class Executor {
  private int apiCallLimit;
  private DBWriter dbwriter;
  private AddressHandler addressHandler;
  private int initialPropertyCount;
  private Validator validator;
  private int maxActiveProperties;
  private Logger logger;

  public Executor(int callLimit, int maxActiveProperties, DBWriter writer,
      AddressHandler addressHandler, Validator validator) {
    this.logger = LogManager.getLogger(this.getClass().getName());
    logger.info("Instantiating Executor with API Call Limit of " + callLimit);
    this.apiCallLimit = callLimit;
    this.dbwriter = writer;
    this.addressHandler = addressHandler;
    this.initialPropertyCount = addressHandler.getPropertyCount();
    this.validator = validator;
    this.maxActiveProperties = maxActiveProperties;
    this.logger = LogManager.getLogger(this.getClass().getName());
  }

  /**
   * 
   */
  public void run() {
    for (int i = 0; i < this.initialPropertyCount; i++) {
      if (this.apiCallLimit > 0) {
        // TODO need to change to method that joins address results with a join on appropriate MSA
        // to be obtained from validator
        Map<String, String> addressToSearch =
            this.addressHandler.getNextAddressToUpdate(this.maxActiveProperties);
        logger.trace("Getting data for address: " + addressToSearch.toString());
        this.getDataForAddress(addressToSearch);
        i++;
        long napTime = Math.round(500.0 * Math.random());
        try {
          Thread.sleep(napTime);
        } catch (InterruptedException e) {
          logger.error("An error occurred while sleeping between addresses.", e);
        }
      }
      logger.info("Finished processing address. API Calls remaining: " + this.apiCallLimit);
    }
  }

  /**
   * Call's Zillow's GetDeepSearchResults API for the given property (as specified by its address
   * and zipcode), and writes the Property data returned to the database. It will also get
   * {@code ZillowComparables} and {@code PropertyDetails} for the property, and will update the
   * address in the database to reflect whether or not any search results were returned.
   * 
   * @param addressToSearch a {@code Map<String, String>} that contains at least key-value pairs for
   *        {@code "address"} and {@code "zip"}
   */
  public void getDataForAddress(Map<String, String> addressToSearch) {
    String address = addressToSearch.get("address");
    String zipcode = addressToSearch.get("zip");

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("address", address);
    params.put("citystatezip", zipcode);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    logger.info("Making API Call to GetDeepSearchResults with params: " + params.toString());
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepSearchResults).addParams(params).build();

    // Make the actual API call
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    try {
      String data = apiTool.makeApiCall(request);
      this.apiCallLimit--;
      logger.info("Request sent to: " + request);

      // Parse the data into a list of properties
      SearchResultParser parser = new SearchResultParser(data);
      ArrayList<DbTableObject> searchResults = parser.parseData();
      logger.info("Extracted " + searchResults.size() + " properties from API call.");

        for (DbTableObject dbto : searchResults) {
          if (dbto instanceof Property) {
            Property p = (Property) dbto;
            BigInteger zpid = p.getZpid();
            this.processPropertyDetails(zpid);
            this.processComps(zpid);
            if (this.validator.validate(p)) {
              p.writeToDB(this.dbwriter);
            }
          }
        }

      if (searchResults.size() > 0) {
        addressToSearch.put("api_result", String.valueOf(true));
      } else {
        addressToSearch.put("api_result", String.valueOf(false));
      }
      this.addressHandler.processAddress(addressToSearch);
    } catch (IOException e) {
      logger.error("IOException occurred while making the api call. \nURL: " + request, e);
    } catch (SAXException e) {
      logger.error("SAXException occurred while parsing the results of the call to " + request, e);
    }
  }


  /**
   * Gets the property details for the property with the given zpid and writes the details to the
   * database
   * 
   * @param zpid The {@code zpid} of the property to search on
   */
  private void processPropertyDetails(BigInteger zpid) {
    String zpidString = zpid.toString();
    logger.trace("Processing property details for property with ZPID " + zpid.toString());

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpidString);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request =
        builder.setBaseUrl(ZillowAPI.GetUpdatedProperyDetails).addParams(params).build();

    // Make the actual API call
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    try {
      String data = apiTool.makeApiCall(request);
      this.apiCallLimit--;

      PropertyDetailParser parser = new PropertyDetailParser(data);
      List<DbTableObject> detailsList = parser.parseData();
      for (DbTableObject dbto : detailsList) {
        dbto.writeToDB(this.dbwriter);
      }
    } catch (IOException e) {
      logger.error(
          "IOException occurred while making the api call for propery details. \nURL: " + request,
          e);
    } catch (SAXException e) {
      logger.error("SAXException occurred while parsing the results of the call to " + request, e);
    }

  }

  /**
   * Gets all comparables for the given property {@code zpid} and writes them to the database, as
   * well as adds their addresses to the Address table with the api_result value set to true
   * 
   * @param zpid The {@code zpid} of the property to get Comparables for
   */
  private void processComps(BigInteger zpid) {
    String zpidString = zpid.toString();
    String count = "25";

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpidString);
    params.put("count", count);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepComps).addParams(params).build();
    System.out.println(request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    try {
      String data = apiTool.makeApiCall(request);
      this.apiCallLimit--;

      GetDeepCompsResultParser parser = new GetDeepCompsResultParser(data);
      List<DbTableObject> comparables = parser.parseData();

      for (DbTableObject dbto : comparables) {
        ZillowComparable c = (ZillowComparable) dbto;
        if (this.validator.validateZip(c.getCompZip())) {
          c.writeToDB(this.dbwriter);
          String compAddress = c.getCompAddress();
          String zip = String.valueOf(c.getCompZip());
          if (compAddress.length() > 1 && zip.length() > 1) {
            boolean api_result = true;
            HashMap<String, String> compAddressValues = new HashMap<String, String>();
            compAddressValues.put("address", compAddress);
            compAddressValues.put("zip", zip);
            compAddressValues.put("api_result", String.valueOf(api_result));
            this.addressHandler.processAddress(compAddressValues);
          } else {
            logger.warn(
                "An address was not validated so is being dropped, and the Comp will not be inserted. Zipcode: "
                    + c.getCompZip());
          }
        }
      }
    } catch (IOException e) {
      logger.error("IOException occurred while making the api call for getting comparables. \nURL: "
          + request, e);
    } catch (SAXException e) {
      logger.error("SAXException occurred while parsing the results of the call to " + request, e);
    }
  }

  /*
   * 1.) Get property to update from Assistant 2.) Call GetDeepSearchResults for that property and
   * write results 3.) Subtract 1 from call Limit 4.) For each property returned, take the zpid and
   * call GetDeepComps 5.) Subtract 1 from call Limit 6.) For each Comparable returned, add the
   * compAddress and compZipcode to Addresses with true 7.) For each property returned, call
   * GetPropertyDetails, and write results DB 8.) Substract 1 from apiCallLimit
   */

}
