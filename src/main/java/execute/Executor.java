/**
 * 
 */
package main.java.execute;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.ZillowComparable;
import main.java.dbConnectors.AddressHandler;
import main.java.dbConnectors.DBWriter;
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

  public Executor(int callLimit, DBWriter writer, AddressHandler addressHandler) {
    this.apiCallLimit = callLimit;
    this.dbwriter = writer;
    this.addressHandler = addressHandler;
    this.initialPropertyCount = addressHandler.getPropertyCount();
  }

  /**
   * 
   */
  public void run() {
    for (int i = 0; i < this.initialPropertyCount; i++) {
      if (this.apiCallLimit > 0) {
        Map<String, String> addressToSearch = this.addressHandler.getNextAddressToUpdate();
        this.getDataForAddress(addressToSearch);
        i++;
        long napTime = Math.round(500.0 * Math.random());
        try {
          Thread.sleep(napTime);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
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
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepSearchResults).addParams(params).build();
    System.out.println(request);
    
    // Make the actual API call
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    this.apiCallLimit--;

    // Parse the data into a list of properties
    SearchResultParser parser = new SearchResultParser(data);
    ArrayList<DbTableObject> searchResults = parser.parseData();

    for (DbTableObject dbto : searchResults) {
      Property p = (Property) dbto;
      BigInteger zpid = p.getZpid();
      this.processComps(zpid);
      this.processPropertyDetails(zpid);
      p.writeToDB(this.dbwriter);
    }

    if (searchResults.size() > 0) {
      addressToSearch.put("api_result", String.valueOf(true));
    } else {
      addressToSearch.put("api_result", String.valueOf(false));
    }
    this.addressHandler.processAddress(addressToSearch);
  }


  /**
   * Gets the property details for the property with the given zpid and writes the details to the
   * database
   * 
   * @param zpid The {@code zpid} of the property to search on
   */
  private void processPropertyDetails(BigInteger zpid) {
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
    this.apiCallLimit--;

    PropertyDetailParser parser = new PropertyDetailParser(data);
    List<DbTableObject> detailsList = parser.parseData();
    for (DbTableObject dbto : detailsList) {
      dbto.writeToDB(this.dbwriter);
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
    String data = apiTool.makeApiCall(request);
    this.apiCallLimit--;

    GetDeepCompsResultParser parser = new GetDeepCompsResultParser(data);
    List<DbTableObject> comparables = parser.parseData();

    for (DbTableObject dbto : comparables) {
      ZillowComparable c = (ZillowComparable) dbto;
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
      }
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
