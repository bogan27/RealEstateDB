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
              unit.setStreetAddress(
                  addressElement.getElementsByTagName("street").item(0).getTextContent());
              unit.setZipCode(
                  addressElement.getElementsByTagName("zipcode").item(0).getTextContent());
              unit.setCity(addressElement.getElementsByTagName("city").item(0).getTextContent());
              unit.setState(addressElement.getElementsByTagName("state").item(0).getTextContent());
              unit.setlatitude(
                  addressElement.getElementsByTagName("latitude").item(0).getTextContent());
              unit.setLongitude(
                  addressElement.getElementsByTagName("longitude").item(0).getTextContent());
            }
          }

          // From Zestimate section of result
          // Sets amount, 30-day change, date last updated, low valuation, high valuation, and
          // percentile rank
          NodeList zestimateList = element.getElementsByTagName("zestimate");
          if (zestimateList.getLength() > 0) {
            Node zestimateNode = zestimateList.item(0);
            if (zestimateNode.getNodeType() == Node.ELEMENT_NODE) {
              Element zestimate = (Element) zestimateNode;
              unit.setZestimate(zestimate.getElementsByTagName("amount").item(0).getTextContent());
              unit.setLastUpdated(
                  zestimate.getElementsByTagName("last-updated").item(0).getTextContent());
              unit.setThirtyDayChange(
                  zestimate.getElementsByTagName("valueChange").item(0).getTextContent());
              unit.setPercentileValue(
                  zestimate.getElementsByTagName("percentile").item(0).getTextContent());
              Element valuationRange =
                  (Element) zestimate.getElementsByTagName("valuationRange").item(0);
              unit.setvaluationLow(
                  valuationRange.getElementsByTagName("low").item(0).getTextContent());
              unit.setValuationHigh(
                  valuationRange.getElementsByTagName("high").item(0).getTextContent());
            }
          }
        }
        propertyList.add(unit);
      }
    }
    return propertyList;
  }

}
