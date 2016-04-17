/**
 * 
 */
package main;

import java.util.Map;

import dbConnectors.AddressHandler;
import dbConnectors.DBWriter;

/**
 * @author brandonbogan
 *
 */
public class Executor {
  private int apiCallLimit;
  private DBWriter dbwriter;
  private AddressHandler addressHandler;

  public Executor(int callLimit, DBWriter writer, AddressHandler addressHandler) {
    this.apiCallLimit = callLimit;
    this.dbwriter = writer;
    this.addressHandler = addressHandler;
  }

  public void run() {
    while (apiCallLimit > 0) {
      Map<String, String> addressToSearch = this.addressHandler.getNextAddressToUpdate();
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
