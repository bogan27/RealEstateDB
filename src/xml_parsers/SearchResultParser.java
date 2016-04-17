package xml_parsers;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import classes_for_db.DbTableObject;
import classes_for_db.Property;

public class SearchResultParser extends ParseZillowResultsAbstract {

  public SearchResultParser(String data) {
    super(data);
  }

  @Override
  public ArrayList<DbTableObject> parseData() {

    ArrayList<DbTableObject> propertyList = new ArrayList<DbTableObject>();

    if (Integer.parseInt(this.getStatusCode()) == 0) {
      Document doc = this.getXmlDoc();
      NodeList resultSet = doc.getElementsByTagName("result");
      for (int i = 0; i < resultSet.getLength(); i++) {
        Property unit = new Property();
        Node iResult = resultSet.item(i);
        if (iResult.getNodeType() == Node.ELEMENT_NODE) {

          Element element = (Element) iResult;

          unit.setZpid(element.getElementsByTagName("zpid").item(0).getTextContent());

          // From Address section of result
          // Sets street address, city, state, zipcode, latitude, and longitude
          NodeList addressSectionList = element.getElementsByTagName("address");
          if (addressSectionList.getLength() > 0) {
            Node addressSection = addressSectionList.item(0);
            if (addressSection.getNodeType() == Node.ELEMENT_NODE) {
              Element addressElement = (Element) addressSection;
              unit.setStreetAddress(this.extractFirstValue(addressElement, "street"));
              unit.setZipCode(this.extractFirstValue(addressElement, "zipcode"));
              unit.setCity(this.extractFirstValue(addressElement, "city"));
              unit.setState(this.extractFirstValue(addressElement, "state"));
              unit.setlatitude(this.extractFirstValue(addressElement, "latitude"));
              unit.setLongitude(this.extractFirstValue(addressElement, "longitude"));
            }
          }

          // Stuff between address and zestimate sections.
          /*
           * Sets: FIPScounty, useCode, yearBuilt, finishedSqFt, bathrooms, bedrooms, tax assessment
           * year, tax assessment amount, date last sold, price last sold for
           */
          unit.setCountyCode(this.extractFirstValue(element, "FIPScounty"));
          unit.setUseCode(this.extractFirstValue(element, "useCode"));
          unit.setTaxYear(this.extractFirstValue(element, "taxAssessmentYear"));
          unit.setTaxAssessment(this.extractFirstValue(element, "taxAssessment"));
          unit.setYearBuilt(this.extractFirstValue(element, "yearBuilt"));
          unit.setFinishedSqFt(this.extractFirstValue(element, "finishedSqFt"));
          unit.setBathroomCount(this.extractFirstValue(element, "bathrooms"));
          unit.setBedroomCount(this.extractFirstValue(element, "bedrooms"));
          unit.setLastSoldDate(this.extractFirstValue(element, "lastSoldDate"));
          unit.setLastSoldPrice(this.extractFirstValue(element, "lastSoldPrice"));


          // From Zestimate section of result
          // Sets amount, 30-day change, date last updated, low valuation, high valuation, and
          // percentile rank
          NodeList zestimateList = element.getElementsByTagName("zestimate");
          if (zestimateList.getLength() > 0) {
            Node zestimateNode = zestimateList.item(0);
            if (zestimateNode.getNodeType() == Node.ELEMENT_NODE) {
              Element zestimate = (Element) zestimateNode;
              unit.setZestimate(this.extractFirstValue(zestimate, "amount"));
              unit.setLastUpdated(this.extractFirstValue(zestimate, "last-updated"));
              unit.setThirtyDayChange(this.extractFirstValue(zestimate, "valueChange"));
              unit.setPercentileValue(this.extractFirstValue(zestimate, "percentile"));
              Element valuationRange =
                  (Element) zestimate.getElementsByTagName("valuationRange").item(0);
              unit.setvaluationLow(this.extractFirstValue(valuationRange, "low"));
              unit.setValuationHigh(this.extractFirstValue(valuationRange, "high"));
            }
          }

          // For Rent Zestimate
          // From Zestimate section of result
          // Sets amount, 30-day change, date last updated, low valuation, high valuation, and
          // percentile rank
          NodeList rentZestimateList = element.getElementsByTagName("rentzestimate");
          if (rentZestimateList.getLength() > 0) {
            Node rentNode = rentZestimateList.item(0);
            if (rentNode.getNodeType() == Node.ELEMENT_NODE) {
              Element rentZ = (Element) rentNode;
              unit.setRentZestimate(this.extractFirstValue(rentZ, "amount"));
              unit.setRentThirtyDayChange(this.extractFirstValue(rentZ, "valueChange"));
              Element valuationRange =
                  (Element) rentZ.getElementsByTagName("valuationRange").item(0);
              unit.setMinRent(this.extractFirstValue(valuationRange, "low"));
              unit.setMaxRent(this.extractFirstValue(valuationRange, "high"));
            }
          }

          // From region section of result. This section is slightly different because the desired
          // values are attribute values, not tag values.
          // Gets region name, id, and type.
          unit.setNameRegion(this.getAttributeValue(element, "region", "name"));
          unit.setRegionID(this.getAttributeValue(element, "region", "id"));
          unit.setRegionType(this.getAttributeValue(element, "region", "type"));
        }
        propertyList.add(unit);
      }
    }
    return propertyList;
  }

}
