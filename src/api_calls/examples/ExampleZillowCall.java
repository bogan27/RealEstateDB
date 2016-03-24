package api_calls.examples;

import java.io.IOException;
import api_calls.ZillowAPI;


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


  /**
   * @param args
   */
  public static void main(String[] args) {
    ZillowAPI setting = ZillowAPI.GetZestimate;
    Example e;
    switch (setting) {
      case GetZestimate:
        e = new GetZestimateExample();
        break;
      case GetSearchResults:
        e = new GetDeepSearchResultsExample();
        break;
      case GetDeepSearchResults:
        e = new GetDeepSearchResultsExample();
        break;
      case GetComps:
        e = new GetDeepSearchResultsExample();
        break;
      case GetDeepComps:
        e = new GetDeepSearchResultsExample();
        break;
      case GetUpdatedProperyDetails:
        e = new GetDeepSearchResultsExample();
        break;
      default:
        e = new GetDeepSearchResultsExample();
    }
    try {
      e.run();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

  }

}
