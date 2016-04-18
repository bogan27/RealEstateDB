package main.java.dbConnectors;

import main.java.classes_for_db.Neighborhood;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.PropertyDetails;
import main.java.classes_for_db.TaxAssessment;
import main.java.classes_for_db.Zestimate;
import main.java.classes_for_db.ZillowComparable;


public interface DBWriter {

  /**
   * Insert a {@code Zestimate} object into a database
   * 
   * @param z The {@code Zestimate} to insert
   */
  void insertObject(Zestimate z);

  /**
   * Insert a {@code Neighborhood} object into a database
   * 
   * @param n The {@code Neighborhood} to insert
   */
  void insertObject(Neighborhood n);

  /**
   * Insert a {@code Property} object into a database
   * 
   * @param p The {@code Property} to insert
   */
  void insertObject(Property p);

  /**
   * Insert a {@code PropertyDetails} object into a database
   * 
   * @param pd The {@code PropertyDetails} to insert
   */
  void insertObject(PropertyDetails pd);

  /**
   * Insert a {@code ZillowComparable} object into a database
   * 
   * @param zc The {@code ZillowComparable} to insert
   */
  void insertObject(ZillowComparable zc);


  /**
   * Insert a {@code TaxAssessment} object into a database
   * 
   * @param ta The {@code TaxAssessment} to insert
   */
  void insertObject(TaxAssessment ta);

}
