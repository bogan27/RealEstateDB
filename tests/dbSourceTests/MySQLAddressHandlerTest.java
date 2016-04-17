/**
 * 
 */
package dbSourceTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import dbConnectors.MySQLAddressHandler;

/**
 * @author brandonbogan
 *
 */
public class MySQLAddressHandlerTest {

  /**
   * Test method for {@link dbConnectors.MySQLAddressHandler#MySQLAddressHandler()}.
   * 
   * Tests that you can successfully instantiate the class and it's super class
   */
  @Test
  public void testMySQLAddressHandler() {
    MySQLAddressHandler testHandler = null;
    try {
      testHandler = new MySQLAddressHandler();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertNotNull(testHandler);
  }

  /**
   * Test method for {@link dbConnectors.MySQLAddressHandler#getNextAddressToUpdate()}.
   */
  @Test
  public void testGetNextAddressToUpdate() {
    MySQLAddressHandler testHandler;
    try {
      testHandler = new MySQLAddressHandler();
      Map<String, String> valuesToSearchOn = testHandler.getNextAddressToUpdate();
      for (String key : valuesToSearchOn.keySet()) {
        System.out.println(key);
        System.out.println(valuesToSearchOn.get(key));
      }
      assertTrue(valuesToSearchOn.containsKey("address"));
      assertTrue(valuesToSearchOn.containsKey("zip"));
      assertTrue(valuesToSearchOn.get("address").length() > 0);
      assertTrue(valuesToSearchOn.get("zip").length() > 0);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * Test method for {@link dbConnectors.MySQLAddressHandler#processAddress(java.util.Map)}.
   */
  @Test
  public void testProcessAddress() {
    MySQLAddressHandler testHandler;
    try {
      testHandler = new MySQLAddressHandler();

      Map<String, String> valuesToProcess = testHandler.getNextAddressToUpdate();
      valuesToProcess.put("api_result", Boolean.toString(true));
      testHandler.processAddress(valuesToProcess);
      Map<String, String> valuesAfterProcess = testHandler.getNextAddressToUpdate();

      String addressBefore = valuesToProcess.get("address");
      System.out.println("Address before: " + addressBefore);
      String addressAfter = valuesAfterProcess.get("address");
      System.out.println("Address after: " + addressAfter);
      assertFalse(addressBefore.equals(addressAfter));

      String zipBefore = valuesToProcess.get("zip");
      String zipAfter = valuesAfterProcess.get("zip");
      assertFalse(zipBefore.equals(zipAfter));

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
