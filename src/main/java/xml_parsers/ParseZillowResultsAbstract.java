/**
 * 
 */
package main.java.xml_parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Property;

/**
 * @author brandonbogan
 *
 */
public abstract class ParseZillowResultsAbstract {
  private String xmlData;
  private Document doc;
  private Logger logger;

  /**
   * @throws IOException 
   * @throws SAXException 
   * 
   */
  public ParseZillowResultsAbstract(String data) throws SAXException, IOException {
    this.xmlData = data;
    this.logger = LogManager.getLogger(this.getClass().getName());
    this.doc = this.convertStringToDoc(data);
  }

  public String getXmlString() {
    return this.xmlData;
  }

  public void setXmlString(String data) throws SAXException, IOException {
    this.xmlData = data;
    this.doc = this.convertStringToDoc(data);
  }

  protected Document getXmlDoc() {
    return this.doc;
  }

  /**
   * Returns the status code of the stored XML, if such a value is found. <br>
   * <br>
   * In order for a status code to be found, the XML must contain code tags in the form of {@code 
   * <code>Status</code>}<br>
   * <br>
   * A status code of {@code 0} means the request was successfully processed.
   * 
   * @return A String representation of the int that is the status code in the XML.
   */
  public String getStatusCode() {
    Element message = (Element) this.doc.getElementsByTagName("message").item(0);
    NodeList codes = message.getElementsByTagName("code");

    String response = "No status code found.";
    if (codes.getLength() > 0) {
      response = codes.item(0).getTextContent();
    }
    return response;
  }

  /**
   * Returns the message text of the XML. The message text can be used for confirming that a request
   * was processed successfully, or for troubleshooting why it was not.<br>
   * <br>
   * If the API call is successful, the message text will read <i>
   * "Request successfully processed"</i>
   * 
   * 
   * @return The message text
   */
  public String getMessageText() {
    String response = "No message text found.";
    NodeList message = this.doc.getElementsByTagName("message");
    for (int i = 0; i < message.getLength(); i++) {
      Node nNode = message.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        response = eElement.getElementsByTagName("text").item(0).getTextContent();
      }
    }
    return response;
  }

  /**
   * Converts a String containing XML into a {@code Document}, which can be used to parse the XML.
   * 
   * @param text The text to be converted into a document
   * @return A Document representing the text
   * @throws IOException 
   * @throws SAXException 
   */
  protected Document convertStringToDoc(String text) throws SAXException, IOException {

    InputSource source = new InputSource(new StringReader(text));

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e1) {
      logger.error("Error in creating the Document Builder docBuilder.", e1);
    }

    Document doc = docBuilder.parse(source);
    doc.getDocumentElement().normalize();
    return doc;
  }

  /**
   * If a tag with the given tag name is found in the given element, this method extracts and
   * returns as a String the value of the first occurrence of the tag. If the tag is not found, null
   * is returned.
   * 
   * @param e An {@code Element} in which to search for the specified tag
   * @param tagName The name of the tag to extract content from
   * @return The value of the first occurrence of the specified tag, if one is found, or
   *         {@code null} if not found.
   */
  public String extractFirstValue(Element e, String tagName) {
    NodeList values = e.getElementsByTagName(tagName);
    String response = "";
    if (values.getLength() > 0) {
      response = values.item(0).getTextContent();
    }
    return response;
  }

  /**
   * Returns the attibute value for the specified attribute on the first occurrence of the specified
   * tag in the given Element.
   * 
   * @param e An {@code Element} in which to search for the specified tag and attribute
   * @param tagName The name of the tag to extract content from
   * @param attribute The name of the attribute whose value should be returned
   * @return The value of the attribute, if found, otherwise {@code null}.
   */
  public String getAttributeValue(Element e, String tagName, String attribute) {
    NodeList nodes = e.getElementsByTagName(tagName);
    String response = null;
    if (nodes.getLength() > 0) {
      response = nodes.item(0).getAttributes().getNamedItem(attribute).getNodeValue();
    }
    return response;
  }
  
  /**
   * Converts the given Element into a fully populated Property instance
   * @param element
   * @return A Property populated with values from child nodes of the given Element
   */
  protected Property convertElementToProperty(Element element) {
    
    Property unit = new Property();
    
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
    
    return unit;
  }

  /**
   * Parses the XML, and assigns the values of tags of interest to fields in the corresponding class
   * that implements DbTableObject
   * 
   * @return A DbTableObject implementation that corresponds to a table in the RealEstateDB.
   */
  public abstract List<DbTableObject> parseData();

}
