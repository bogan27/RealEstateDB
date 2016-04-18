/**
 * 
 */
package main.java.dbConnectors;

import java.util.Map;

/**
 * @author brandonbogan
 *
 */
public interface AddressHandler {

  /**
   * Returns a Map containing information on the next address to process. <br>
   * <br>
   * The Map will contain at least two key-value pairs:
   * <ol>
   * <li>"address", the street address of the property</li>
   * <li>"zip", the zipcode of the property, stored as a String</li>
   * </ol>
   * Any properties that do not have a value for the field api_result (the field that indicates
   * whether or not search results were found for the address the last time it was tried) will be
   * selected first. If all Properties have a date for when they were last updated, the property
   * with the oldest date will be returned
   * 
   * @return
   */
  public Map<String, String> getNextAddressToUpdate();

  /**
   * Given a Map, this method will look for the following Keys and insert their values into the
   * Address table:
   * <ul>
   * <li>{@code "address"} (will be left as a String)</li>
   * <li>{@code "zip"} (will be converted to an int)</li>
   * <li>{@code "api_result"} (will be converted to a boolean</li>
   * </ul>
   * 
   */
  public void processAddress(Map<String, String> values);

  /**
   * Gets a count of the number of addresses that are currently in the Addresses table
   * 
   * @return The count of addresses
   */
  public int getPropertyCount();
}
