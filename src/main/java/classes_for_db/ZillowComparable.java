/**
 * 
 */
package main.java.classes_for_db;

import java.math.BigInteger;
import java.util.List;

import main.java.dbConnectors.DBWriter;

/**
 * @author brandonbogan
 *
 */
public class ZillowComparable implements DbTableObject {

  private BigInteger compID;
  private BigInteger primaryZPID;
  private BigInteger compZPID;
  private float compScore;
  private String compAddress;
  private int compZip;

  /**
   * 
   */
  public ZillowComparable() {
    // TODO Auto-generated constructor stub
  }

  public BigInteger getCompID() {
    return compID;
  }

  public void setCompID(BigInteger id) {
    BigInteger min = new BigInteger("0");
    BigInteger max = new BigInteger("1000000000000");
    if (id.compareTo(min) < 0) {
      throw new IllegalArgumentException(
          "Comp ID cannot be negative. Argument given for Comp ID: " + id);
    }
    if (id.compareTo(max) >= 0) {
      throw new IllegalArgumentException(
          "Argument given for Comp ID exceeds maximum allowed value of 1000000000000 (1 trillion). Argument given: "
              + id);
    } else {
      this.compID = id;
    }
  }

  public BigInteger getPrimaryZpid() {
    return primaryZPID;
  }



  /**
   * 
   * @param primaryZPID A BigInteger representing the primaryZPID of the property. This argument
   *        needs to be a {@code BigInteger} instead of an {@code int} because the size of some of
   *        the larger primaryZPIDs exceeds the max value of an int.
   * @throws IllegalArgumentException if {@code primaryZPID} is less than 0 or greater than
   *         10000000000 (10 billion).
   */
  public void setPrimaryZPID(BigInteger primaryZPID) {
    BigInteger min = new BigInteger("0");
    BigInteger max = new BigInteger("10000000000");
    if (primaryZPID.compareTo(min) < 0) {
      throw new IllegalArgumentException(
          "primaryZPID cannot be negative. Argument given for primaryZPID: " + primaryZPID);
    }
    if (primaryZPID.compareTo(max) >= 0) {
      throw new IllegalArgumentException(
          "Argument given for primaryZPID exceeds maximum allowed value of 10000000000 (10 billion). Argument given: "
              + primaryZPID);
    } else {
      this.primaryZPID = primaryZPID;
    }
  }

  /**
   * 
   * @param primaryZPID A String representation of this property's primaryZPID
   * @throws IllegalArgumentException if {@code primaryZPID} is longer than 11 characters, is less
   *         than 0, or is greater than 10000000000 (10 billion).
   */
  public void setPrimaryZPID(String primaryZPID) {
    if (primaryZPID.length() > 11) {
      throw new IllegalArgumentException(
          "Argument for primaryZPID exceeds maximum allowable length of 11 characters. Argument given: "
              + primaryZPID);
    } else {
      BigInteger id = new BigInteger(primaryZPID);
      this.setPrimaryZPID(id);
    }
  }


  public BigInteger getCompZpid() {
    return compZPID;
  }


  /**
   * 
   * @param compZPID A BigInteger representing the compZPID of the property. This argument needs to
   *        be a {@code BigInteger} instead of an {@code int} because the size of some of the larger
   *        compZPIDs exceeds the max value of an int.
   * @throws IllegalArgumentException if {@code compZPID} is less than 0 or greater than 10000000000
   *         (10 billion).
   */
  public void setCompZPID(BigInteger compZPID) {
    BigInteger min = new BigInteger("0");
    BigInteger max = new BigInteger("10000000000");
    if (compZPID.compareTo(min) < 0) {
      throw new IllegalArgumentException(
          "compZPID cannot be negative. Argument given for compZPID: " + compZPID);
    }
    if (compZPID.compareTo(max) >= 0) {
      throw new IllegalArgumentException(
          "Argument given for compZPID exceeds maximum allowed value of 10000000000 (10 billion). Argument given: "
              + compZPID);
    } else {
      this.compZPID = compZPID;
    }
  }

  /**
   * 
   * @param compZPID A String representation of this property's compZPID
   * @throws IllegalArgumentException if {@code compZPID} is longer than 11 characters, is less than
   *         0, or is greater than 10000000000 (10 billion).
   */
  public void setCompZPID(String compZPID) {
    if (compZPID.length() > 11) {
      throw new IllegalArgumentException(
          "Argument for compZPID exceeds maximum allowable length of 11 characters. Argument given: "
              + compZPID);
    } else {
      BigInteger id = new BigInteger(compZPID);
      this.setCompZPID(id);
    }
  }



  /**
   * @return the compScore
   */
  public float getCompScore() {
    return compScore;
  }

  /**
   * @param compScore the compScore to set
   * @throws IllegalArgumentException if {@code compScore} is less than 0 or greater than 1
   */
  public void setCompScore(float compScore) {
    if (compScore < 0 | compScore > 100) {
      throw new IllegalArgumentException(
          "Argument for compScore falls outside of allowed range of 0 to 1. Argument given: "
              + compScore);
    }
    this.compScore = compScore;
  }

  /**
   * @param compScore the compScore to set
   * @throws IllegalArgumentException if {@code compScore} is less than 0 or greater than 1
   */
  public void setCompScore(String compScore) {
    this.setCompScore(Float.parseFloat(compScore));
  }

  /**
   * @return the compAddress
   */
  public String getCompAddress() {
    return compAddress;
  }

  /**
   * @param compAddress the compAddress to set
   */
  public void setCompAddress(String compAddress) {
    this.compAddress = compAddress;
  }

  /**
   * @return the compZip
   */
  public int getCompZip() {
    return compZip;
  }

  /**
   * @param compZip the compZip to set
   */
  public void setCompZip(int compZip) {
    if (compZip < 0 | compZip > 99999) {
      throw new IllegalArgumentException(
          "Invalid argument given for zipcode of comparable: " + compZip);
    }
    this.compZip = compZip;
  }

  /**
   * @param compZip the compZip to be converted from a String to an int and then set
   */
  public void setCompZip(String compZipAsString) {
    if (compZipAsString.trim().length() > 0) {
      int zip = Integer.parseInt(compZipAsString);
      this.setCompZip(zip);
    } else {
      this.setCompZip(0);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Primary ZPID: " + this.getPrimaryZpid() + "\n");
    sb.append("Comparable ZPID: " + this.getCompZpid() + "\n");
    sb.append("Comparability Score: " + this.getCompScore() + "\n");
    sb.append("Comp Street Address: " + this.compAddress + "\n");
    sb.append("Comp Zip: " + this.compZip + "\n");
    sb.append("\n\n");
    return sb.toString();
  }

  @Override
  public boolean writeToDB(DBWriter writer) {
    writer.insertObject(this);
    return true;
  }


}
