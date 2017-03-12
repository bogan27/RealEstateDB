/**
 * 
 */
package main.java.api_calls.examples;

import java.io.IOException;
import java.util.HashMap;

import org.xml.sax.SAXException;

import main.java.api_calls.GenericZillowAPICaller;
import main.java.api_calls.ZillowAPI;
import main.java.api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.PropertyDetails;
import main.java.xml_parsers.PropertyDetailParser;

/**
 * @author brandonbogan
 *
 */
public class GetPropertyDetailsExample implements Example {

  /**
   * 
   */
  public GetPropertyDetailsExample() {
    // TODO Auto-generated constructor stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see api_calls.examples.Example#run()
   */
  @Override
  public void run() throws IOException {
    // String zpid = "81858764";
    String zpid = "123844485";

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("zpid", zpid);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request =
        builder.setBaseUrl(ZillowAPI.GetUpdatedProperyDetails).addParams(params).build();
    System.out.println(request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
    // System.out.println(data);
    // System.out.println(request);

    try {
      PropertyDetailParser parser = new PropertyDetailParser(data);
      System.out.println("Response status code: " + parser.getStatusCode());
      System.out.println("Response message text: " + parser.getMessageText());
      for (DbTableObject dbto : parser.parseData()) {
        PropertyDetails details = (PropertyDetails) dbto;
        System.out.println(details.toString());
      }
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
