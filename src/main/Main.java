/**
 * 
 */
package main;

import java.sql.SQLException;

import dbConnectors.AddressHandler;
import dbConnectors.DBWriter;
import dbConnectors.MySQLAddressHandler;
import dbConnectors.MysqlWriter;

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
