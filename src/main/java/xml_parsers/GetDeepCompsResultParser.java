/**
 * 
 */
package main.java.xml_parsers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.ZillowComparable;

/**
 * @author brandonbogan
 *
 */
public class GetDeepCompsResultParser extends ParseZillowResultsAbstract {

  /**
   * @param data
   */
  public GetDeepCompsResultParser(String data) {
    super(data);
    // TODO Auto-generated constructor stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see xml_parsers.ParseZillowResultsAbstract#parseData()
   */
  @Override
  public List<DbTableObject> parseData() {
    List<DbTableObject> compList = new ArrayList<DbTableObject>();
    if (Integer.parseInt(this.getStatusCode()) == 0) {
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
          Element compNode = (Element) compI;
          ZillowComparable comp = new ZillowComparable();
          comp.setPrimaryZPID(principalZPID);
          comp.setCompScore(compNode.getAttribute("score"));
          comp.setCompZPID(this.extractFirstValue(compNode, "zpid"));
          compList.add(comp);

          Element addressSection = (Element) compNode.getElementsByTagName("address").item(0);
          comp.setCompAddress(this.extractFirstValue(addressSection, "street"));
          comp.setCompZip(this.extractFirstValue(addressSection, "zipcode"));
        }
      }
    }
    return compList;
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
