/**
 * 
 */
package api_calls.examples;

import java.io.IOException;
import java.util.HashMap;

import api_calls.GenericZillowAPICaller;
import api_calls.ZillowAPI;
import api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import classes_for_db.DbTableObject;
import classes_for_db.Zestimate;
import dbConnectors.DBWriter;
import dbConnectors.MysqlWriter;
import xml_parsers.GetZestimateResultParser;

/**
 * @author brandonbogan
 *
 */
public class GetZestimateExample implements Example {

  /**
   * 
   */
  public GetZestimateExample() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void run() throws IOException {
    String zpid = "81858764";

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpid);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetZestimate).addParams(params).build();
    System.out.println(request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    // System.out.println(data);
    // System.out.println(request);

    GetZestimateResultParser parser = new GetZestimateResultParser(data);
    System.out.println("Response status code: " + parser.getStatusCode());
    System.out.println("Response message text: " + parser.getMessageText());
    DBWriter dbw = new MysqlWriter();
    for (DbTableObject dbto : parser.parseData()) {
      Zestimate z = (Zestimate) dbto;
      dbw.insertObject(z);
      System.out.println(z.toString());
    }

  }


}
