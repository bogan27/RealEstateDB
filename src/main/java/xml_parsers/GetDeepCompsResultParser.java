/**
 * 
 */
package main.java.xml_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.Property;
import main.java.classes_for_db.ZillowComparable;

/**
 * @author brandonbogan
 *
 */
public class GetDeepCompsResultParser extends ParseZillowResultsAbstract {

  private boolean returnProperties = false;
  private boolean returnComps = true;

  /**
   * @param data
   * @throws SAXException 
   */
  public GetDeepCompsResultParser(String data) throws IOException, SAXException {
    super(data);
  }

  /**
   * @return If the parser will return DBTableObjects of the Property class (default if false)
   */
  public boolean toReturnProperties() {
    return returnProperties;
  }

  /**
   * @param returnProperties should the parser return DBTableObjects of the Property class (default is false)
   */
  public void setReturnProperties(boolean returnProperties) {
    this.returnProperties = returnProperties;
  }

  /**
   * @return If the parser will return DBTableObjects of the Comparable class (default if true)
   */
  public boolean isReturnComps() {
    return returnComps;
  }

  /**
   * @param returnComps If the parser will return DBTableObjects of the Comparable class (default if true)
   */
  public void setReturnComps(boolean returnComps) {
    this.returnComps = returnComps;
  }

  /*
   * (non-Javadoc)
   * 
   * @see xml_parsers.ParseZillowResultsAbstract#parseData()
   */
  @Override
  public List<DbTableObject> parseData() {
    List<DbTableObject> resultList = new ArrayList<DbTableObject>();
    if (this.returnComps && Integer.parseInt(this.getStatusCode()) == 0) {
      Document doc = this.getXmlDoc();
      Element response = (Element) doc.getElementsByTagName("response").item(0);
      Element properties = (Element) response.getElementsByTagName("properties").item(0);
      String principalZPID = this.getPrincipalZPID(properties);
      // Element list = (Element) response.getElementsByTagName("comparables").item(0);
      NodeList comparableList = properties.getElementsByTagName("comp");
      System.out.println("Size of comparablesList: " + comparableList.getLength());
      for (int i = 0; i < comparableList.getLength(); i++) {
        Node compI = comparableList.item(i);
        if (compI.getNodeType() == Node.ELEMENT_NODE) {
          resultList.add(this.convertElementToComparable(principalZPID, (Element) compI));
        }
      }
    } 
    if(this.returnProperties && Integer.parseInt(this.getStatusCode()) == 0){
      Document doc = this.getXmlDoc();
      Element principal = (Element) doc.getElementsByTagName("principal").item(0);
      Property principalProperty = this.convertElementToProperty(principal);
      NodeList resultSet = doc.getElementsByTagName("comp");
      List<Property> properties = new ArrayList<Property>();
      for (int i = 0; i < resultSet.getLength(); i++) {
        Node iResult = resultSet.item(i);
        if (iResult.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) iResult;
          properties.add(this.convertElementToProperty(element));
          principalProperty.addComp(this.convertElementToComparable(principalProperty.getZpid().toString(), element));
        }
      }
      resultList.add(principalProperty);
      resultList.addAll(properties);
    }
    return resultList;
  }

  private ZillowComparable convertElementToComparable(String principalZPID, Element compElement) {
    ZillowComparable comp = new ZillowComparable();
    comp.setPrimaryZPID(principalZPID);
    comp.setCompScore(compElement.getAttribute("score"));
    comp.setCompZPID(this.extractFirstValue(compElement, "zpid"));

    Element addressSection = (Element) compElement.getElementsByTagName("address").item(0);
    comp.setCompAddress(this.extractFirstValue(addressSection, "street"));
    comp.setCompZip(this.extractFirstValue(addressSection, "zipcode"));
    return comp;
  }

  private String getPrincipalZPID(Element root) {
    String response = null;
    NodeList principals = root.getElementsByTagName("principal");
    if (principals.getLength() > 0) {
      Element principal = (Element) principals.item(0);
      response = this.extractFirstValue(principal, "zpid");
    }
    return response;
  }

}
