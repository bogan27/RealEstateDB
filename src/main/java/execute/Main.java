/**
 * 
 */
package main.java.execute;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.dbConnectors.AddressHandler;
import main.java.dbConnectors.BostonMSAValidator;
import main.java.dbConnectors.DBWriter;
import main.java.dbConnectors.MySQLAddressHandler;
import main.java.dbConnectors.MysqlWriter;
import main.java.dbConnectors.Validator;

/**
 * @author brandonbogan
 *
 */
public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Logger logger = LogManager.getLogger("main.java.execute.Main");
    int apiCallLimit = 500;
    int maxComps = 25;
    int maxActiveProperties = 10000;
    Validator v = new BostonMSAValidator();
//    Queue<String> zpids = new LinkedList<String>();
////    zpids.add("2103049562");
//    zpids.add("123844485");
//    try {
//      ZillowSpider spider = new ZillowSpider();
//      spider.spider(zpids, apiCallLimit, maxComps, v);
//    } catch (SQLException e) {
//      System.out.println("Error creating spider.");
//      e.printStackTrace();
//    }
    logger.info("Beginning Zillow Spidering");
    DBWriter writer = null;
    AddressHandler ah = null;
    try {
      writer = new MysqlWriter();
      ah = new MySQLAddressHandler();
    } catch (SQLException e) {
      logger.error("A SQLExcepion occurred while creating the DBWriter and AddressHandler.", e);
    }
    Executor exec = new Executor(apiCallLimit, maxActiveProperties, writer, ah, v);
    exec.run();
  }

}
