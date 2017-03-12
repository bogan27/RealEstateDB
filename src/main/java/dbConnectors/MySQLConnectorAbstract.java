/**
 * 
 */
package main.java.dbConnectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author brandonbogan
 *
 */
public abstract class MySQLConnectorAbstract {

  protected final Logger logger = LogManager.getLogger(this.getClass().getName());

  /** The name of the MySQL account to use (or empty for anonymous) */
  protected final String userName = "mysqlUser";

  /** The password for the MySQL account (or empty for anonymous) */
  protected final String password = "zillow";

  /** The name of the computer running MySQL */
  protected final String serverName = "54.86.82.1";

  /** The port of the MySQL server (default is 3306) */
  protected final int portNumber = 3306;

  /** the DB name */
  protected final String dbName = "RealEstate";

  protected Connection connect;

  protected Connection getConnection() throws SQLException {

    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);
    String connectionString =
        "jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName;
    System.out.println(connectionString);
    conn = DriverManager.getConnection(connectionString, connectionProps);
    this.connect = conn;
    logger.trace("Successfully connected to Database.");
    return conn;
  }

  /**
   * Run a SQL command which does not return a recordset: CREATE/INSERT/UPDATE/DELETE/DROP/etc.
   * 
   * @throws SQLException If something goes wrong
   */
  protected boolean executeUpdate(Connection conn, String command) throws SQLException {
    Statement stmt = null;
    try {
      logger.trace("Executing the following query: " + command);
      stmt = conn.createStatement();
      stmt.executeUpdate(command); // This will throw a SQLException if it fails
      logger.info("Successfully executed query: " + command);
      return true;
    } finally {

      // This will run whether we throw an exception or not
      if (stmt != null) {
        stmt.close();
        logger.trace("Closing Statement after executing query: " + command);
      }
    }
  }

}
