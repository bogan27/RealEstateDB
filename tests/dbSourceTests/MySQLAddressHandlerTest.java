/**
 * 
 */
package dbSourceTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;

import DBSource.MySQLAddressHandler;

/**
 * @author brandonbogan
 *
 */
public class MySQLAddressHandlerTest {

  /**
   * Test method for {@link DBSource.MySQLAddressHandler#MySQLAddressHandler()}.
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
   * Test method for {@link DBSource.MySQLAddressHandler#getNextAddressToUpdate()}.
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   * Test method for {@link DBSource.MySQLAddressHandler#processAddress(java.util.Map)}.
   */
  @Test
  public void testProcessAddress() {
    fail("Not yet implemented"); // TODO
  }

}
