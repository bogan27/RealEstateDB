/**
 * 
 */
package api_calls.examples;

import java.io.IOException;
import java.util.HashMap;

import DBSource.DBWriter;
import DBSource.MysqlWriter;
import api_calls.GenericZillowAPICaller;
import api_calls.ZillowAPI;
import api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import classes_for_db.DbTableObject;
import classes_for_db.Property;
import xml_parsers.SearchResultParser;

/**
 * @author brandonbogan
 *
 */
public class GetDeepSearchResultsExample implements Example {

  /**
   * 
   */
  public GetDeepSearchResultsExample() {
    // TODO Auto-generated constructor stub
  }

  public void run() throws IOException {
    String address = "580 Washington St";// "99 Pond Ave";
    String zipCode = "02111";// "02445";

   // String address = "99 Pond Ave";
   // String zipCode = "02445";

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("address", address);
    params.put("citystatezip", zipCode);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepSearchResults).addParams(params).build();
    System.out.println(request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    // System.out.println(data);
    // System.out.println(request);

    SearchResultParser parser = new SearchResultParser(data);
    System.out.println("Response status code: " + parser.getStatusCode());
    System.out.println("Response message text: " + parser.getMessageText());
    DBWriter dbw = new MysqlWriter();
    for (DbTableObject dbto : parser.parseData()) {
      Property p = (Property) dbto;
      p.writeToDB(dbw);
      // Property p = (Property) parser.parseData().get(0);
      System.out.println(p.toString());
    }
  }

}
