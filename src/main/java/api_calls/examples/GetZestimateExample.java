/**
 * 
 */
package main.java.api_calls.examples;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.xml.sax.SAXException;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Zestimate;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.MysqlWriter;
import main.java.xml_parsers.GetZestimateResultParser;

/**
 * @author brandonbogan
 *
 */
public class GetZestimateExample implements Example {

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


    DBWriter dbw;
    try {
      GetZestimateResultParser parser = new GetZestimateResultParser(data);
      System.out.println("Response status code: " + parser.getStatusCode());
      System.out.println("Response message text: " + parser.getMessageText());
      dbw = new MysqlWriter();
      for (DbTableObject dbto : parser.parseData()) {
        Zestimate z = (Zestimate) dbto;
        dbw.insertObject(z);
        System.out.println(z.toString());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


}
