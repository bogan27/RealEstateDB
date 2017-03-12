package main.java.dbConnectors;

import main.java.classes_for_db.Property;

public interface Validator {

  /**
   * @param property The property to validate
   * @return Whether or not the given Property is valid according to the criteria defined within the
   *         given {@code Validator} implementation
   */
  public boolean validate(Property property);


  /**
   * @param zipcode the zipcode to validate
   * @return Whether or not the given zipcode is valid according to the criteria defined within the
   *         given {@code Validator} implementation
   */
  public boolean validateZip(int zipcode);
}
