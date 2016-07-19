package main.java.dbConnectors;

import main.java.classes_for_db.Property;

public interface Validator {
	
	public boolean validateGeo(Property property, String geo);
	public boolean validateComp(Property property);
}
