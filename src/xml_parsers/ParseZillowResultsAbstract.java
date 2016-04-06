/**
 * 
 */
package xml_parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import classes_for_db.DbTableObject;

/**
 * @author brandonbogan
 *
 */
public abstract class ParseZillowResultsAbstract {
  private String xmlData;
  private Document doc;

  /**
   * 
   */
  public ParseZillowResultsAbstract(String data) {
    this.xmlData = data;
    this.doc = this.convertStringToDoc(data);
  }

  public String getXmlString() {
    return this.xmlData;
  }

  public void setXmlString(String data) {
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
   */
  protected Document convertStringToDoc(String text) {

    InputSource source = new InputSource(new StringReader(text));

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e1) {
      e1.printStackTrace();
      System.out.println("Error in creating the Document Builder docBuilder.");
    }

    Document doc = null;
    try {
      doc = docBuilder.parse(source);
    } catch (SAXException | IOException e) {
      e.printStackTrace();
      System.out.println("Error in parsing the XML.");
    }
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
   * Parses the XML, and assigns the values of tags of interest to fields in the corresponding class
   * that implements DbTableObject
   * 
   * @return A DbTableObject implementation that corresponds to a table in the RealEstateDB.
   */
  public abstract List<DbTableObject> parseData();

}
