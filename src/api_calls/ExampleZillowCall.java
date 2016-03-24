package api_calls;

import java.io.IOException;
import java.util.HashMap;

import api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
import classes_for_db.DbTableObject;
import classes_for_db.Property;
import xml_parsers.SearchResultParser;

// import okhttp3.HttpUrl;
// import okhttp3.OkHttpClient;
// import okhttp3.Request;
// import okhttp3.Response;


/**
 * 
 */

/**
 * @author brandonbogan
 *
 */
public class ExampleZillowCall {

  /**
   * 
   */
  public ExampleZillowCall() {
    // TODO Auto-generated constructor stub
  }


  public static void getHomes() throws IOException {
    String address = "210 South Street";// "99 Pond Ave";
    String zipCode = "02111";// "02445";
    
//    String address = "99 Pond Ave";
//    String zipCode = "02445";

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
    for (DbTableObject dbto : parser.parseData()) {
      Property p = (Property) dbto;
      // Property p = (Property) parser.parseData().get(0);
      System.out.println(p.toString());
    }
  }


  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      getHomes();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
