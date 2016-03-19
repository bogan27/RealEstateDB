package api_calls;

import java.io.IOException;
import java.util.HashMap;

import api_calls.GenericZillowAPICaller.ZillowRequestBuilder;
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
    String address = "99 Pond Ave";
    String zipCode = "02445";

    // Create a map of parameter names and values
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("address", address);
    params.put("citystatezip", zipCode);

    // Create a new ZillowRequestBuilder, and set the necessary values on it
    ZillowRequestBuilder builder = new GenericZillowAPICaller.ZillowRequestBuilder();
    String request = builder.setBaseUrl(ZillowAPI.GetDeepSearchResults).addParams(params).build();
//    System.out.println(request);

    // Make the actual API call, and print out the data
    GenericZillowAPICaller apiTool = new GenericZillowAPICaller();
    String data = apiTool.makeApiCall(request);
//    System.out.println(data);
//    System.out.println(request);

    SearchResultParser parser = new SearchResultParser(data);
    System.out.println("Response status code: " + parser.getStatusCode());
    System.out.println("Response message text: " + parser.getMessageText());
    Property p = (Property) parser.parseData().get(0);
    System.out.println("ZPID: " + p.getZpid());
    System.out.println("Zip code: " + p.getZipCode());
    System.out.println("Street Address: " + p.getStreetAddress());
    System.out.println("City: " + p.getCity());
    System.out.println("State: " + p.getState());
    System.out.println("Latitude: " + p.getLatitude());
    System.out.println("Longitude: " + p.getLongitude());
    
  }


  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      getHomes();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
