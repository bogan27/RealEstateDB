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
public class TaxAssessment implements DbTableObject {
  private BigInteger assessmentID;
  private BigInteger zpid;
  private int taxYear;
  private float taxAssessment;


  public BigInteger getAssessmentID() {
    return assessmentID;
  }

  public void setAssessmentID(BigInteger assID) {
    BigInteger min = new BigInteger("0");
    BigInteger max = new BigInteger("1000000000000");
    if (assID.compareTo(min) < 0) {
      throw new IllegalArgumentException(
          "assessment ID cannot be negative. Argument given for assessment ID: " + assID);
    }
    if (assID.compareTo(max) >= 0) {
      throw new IllegalArgumentException(
          "Argument given for assessmentID exceeds maximum allowed value of 1000000000000 (1 trillion). Argument given: "
              + assID);
    } else {
      this.assessmentID = assID;
    }
  }


  /**
   * 
   * @param zpid A BigInteger representing the zpid of the property. This argument needs to be a
   *        {@code BigInteger} instead of an {@code int} because the size of some of the larger
   *        zpids exceeds the max value of an int.
   * @throws IllegalArgumentException if {@code zpid} is less than 0 or greater than 10000000000 (10
   *         billion).
   */

  public BigInteger getZpid() {
    return zpid;
  }

  public void setZpid(BigInteger zpid) {
    BigInteger min = new BigInteger("0");
    BigInteger max = new BigInteger("10000000000");
    if (zpid.compareTo(min) < 0) {
      throw new IllegalArgumentException(
          "zpid cannot be negative. Argument given for zpid: " + zpid);
    }
    if (zpid.compareTo(max) >= 0) {
      throw new IllegalArgumentException(
          "Argument given for zpid exceeds maximum allowed value of 10000000000 (10 billion). Argument given: "
              + zpid);
    } else {
      this.zpid = zpid;
    }
  }

  /**
   * 
   * @param zpid A String representation of this property's zpid
   * @throws IllegalArgumentException if {@code zpid} is longer than 11 characters, is less than 0,
   *         or is greater than 10000000000 (10 billion).
   */
  public void setZpid(String zpid) {
    if (zpid.length() > 11) {
      throw new IllegalArgumentException(
          "Argument for zpid exceeds maximum allowable length of 11 characters. Argument given: "
              + zpid);
    } else {
      BigInteger id = new BigInteger(zpid);
      this.setZpid(id);
    }
  }


  /**
   * @return the taxYear
   */
  public int getTaxYear() {
    return taxYear;
  }

  /**
   * @param taxYear the taxYear to set
   * @throws IllegalArgumentException if {@code taxYear} is less than 1900 or greater than 2017
   */
  public void setTaxYear(int taxYear) {
    if (taxYear < 1900 | taxYear > 2017) {
      this.taxYear = 0;
    }
    this.taxYear = taxYear;
  }

  /**
   * @param taxYear the taxYear to set
   * @throws IllegalArgumentException if {@code taxYear} is less than 1900 or greater than 2017
   */
  public void setTaxYear(String taxYear) {
    if (taxYear.length() > 4) {
      throw new IllegalArgumentException(
          "Argument for taxYear exceeds max allowed length of 4 characters. Argument given: "
              + taxYear);
    } else if (taxYear.length() > 0) {
      this.setTaxYear(Integer.parseInt(taxYear));
    }
  }

  /**
   * @return the taxAssessment
   */
  public float getTaxAssessment() {
    return taxAssessment;
  }

  /**
   * @param taxAssessment the taxAssessment to set
   */
  public void setTaxAssessment(float taxAssessment) {
    this.taxAssessment = taxAssessment;
  }

  /**
   * @param taxAssessment the taxAssessment to set
   */
  public void setTaxAssessment(String taxAssessment) {
    if (taxAssessment.length() > 0) {
      this.setTaxAssessment(Float.parseFloat(taxAssessment));
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ZPID: " + this.zpid + "\n");
    sb.append("Tax Assessment Year: " + this.taxYear + "\n");
    sb.append("Tax Assessment Amount: " + this.taxAssessment + "\n");
    return sb.toString();
  }

  @Override
  public boolean writeToDB(DBWriter writer) {
    writer.insertObject(this);
    return true;
  }

}
