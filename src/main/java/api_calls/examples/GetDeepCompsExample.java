/**
 * 
 */
package main.java.api_calls.examples;

import java.io.IOException;
import java.util.HashMap;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.ZillowComparable;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.MysqlWriter;
import main.java.xml_parsers.GetDeepCompsResultParser;

/**
 * @author brandonbogan
 *
 */
public class GetDeepCompsExample implements Example {

  /**
   * 
   */
  public GetDeepCompsExample() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see api_calls.examples.Example#run()
   */
  @Override
  public void run() throws IOException {
    String zpid = "123844485";// "81858764";
    String count = "25";

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpid);
    params.put("count", count);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepComps).addParams(params).build();
    System.out.println(request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    // System.out.println(data);
    // System.out.println(request);

    GetDeepCompsResultParser parser = new GetDeepCompsResultParser(data);
    System.out.println("Response status code: " + parser.getStatusCode());
    System.out.println("Response message text: " + parser.getMessageText());
    DBWriter db = new MysqlWriter();
    for (DbTableObject dbto : parser.parseData()) {
      ZillowComparable c = (ZillowComparable) dbto;
      db.insertObject(c);
      System.out.println(c.toString());
    }

  }

}
