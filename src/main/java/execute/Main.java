/**
 * 
 */
package main.java.execute;

import java.sql.SQLException;

import main.java.dbConnectors.AddressHandler;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.MySQLAddressHandler;
import main.java.dbConnectors.MysqlWriter;

/**
 * @author brandonbogan
 *
 */
public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    int apiCallLimit = 1000;
    DBWriter writer = new MysqlWriter();
    AddressHandler ah = null;
    try {
      ah = new MySQLAddressHandler();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Executor exec = new Executor(apiCallLimit, writer, ah);
    exec.run();
  }

}
