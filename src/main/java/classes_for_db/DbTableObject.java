package main.java.classes_for_db;

import main.java.dbConnectors.DBWriter;

public interface DbTableObject {

  /**
   * Insert {@code this} DBTableObject into a database specified by the given {@code DBWriter}
   * 
   * @param writer The {@code DBWriter} to handle the connection to the database and perform the
   *        insertion
   * @return {@code true} if the object is successfully written to the database or the object
   *         already exists in the database, or {@code false} if the database operation is not
   *         successful
   */
  public boolean writeToDB(DBWriter writer);
}
