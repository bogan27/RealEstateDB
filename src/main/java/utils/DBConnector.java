/**
 * 
 */
package main.java.utils;

import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Neighborhood;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.PropertyDetails;
import main.java.classes_for_db.TaxAssessment;
import main.java.classes_for_db.Zestimate;

/**
 * @author brandonbogan
 *
 */
public interface DBConnector {
  public void handleNewDBTableObject(DbTableObject object);

  public void handleNewProperty(Property property);

  public void handleNewZestimate(Zestimate zestimate);

  public void handleNewNeighborhood(Neighborhood hood);

  public void handleNewComparable(Comparable comp);

  public void handleNewTaxAssessment(TaxAssessment assessment);

  public void handleNewPropertyDetail(PropertyDetails details);
}
