package main.java.xml_parsers;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ExampleXMLParser {


  public ExampleXMLParser() {
    // TODO Auto-generated constructor stub
  }

  public static void parseXMLString() {
    String xml = "<resp><status>good</status><msg>hi</msg></resp>";

    InputSource source = new InputSource(new StringReader(xml));

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      System.out.println("Error creating document builder.");
    }
    Document document = null;
    try {
      document = db.parse(source);
    } catch (SAXException | IOException e) {
      e.printStackTrace();
      System.out.println("Error in parsing the input source.");
    }

    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();

    String msg = null;
    try {
      msg = xpath.evaluate("/resp/msg", document);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
      System.out.println("Error evaluating XPath.");
    }
    String status = null;
    try {
      status = xpath.evaluate("/resp/status", document);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
      System.out.println("Error evaluating XPath.");
    }

    System.out.println("msg=" + msg + ";" + "status=" + status);
  }

  public static void parseXmlWithRepeatTags() {
    String xml = "<resp><status>good</status><status>bad</status><msg>hi</msg></resp>";
    InputSource source = new InputSource(new StringReader(xml));

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    Document doc = null;
    try {
      doc = docBuilder.parse(source);
    } catch (SAXException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    doc.getDocumentElement().normalize();

    System.out.println("Root element : " + doc.getDocumentElement().getNodeName());

    NodeList nList = doc.getElementsByTagName("resp");
    System.out.println("----------------------------");
    for (int temp = 0; temp < nList.getLength(); temp++) {

      Node nNode = nList.item(temp);

      System.out.println("\nCurrent Element :" + nNode.getNodeName());

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {

        Element eElement = (Element) nNode;

        NodeList statuses = eElement.getElementsByTagName("status");
        for (int i = 0; i < statuses.getLength(); i++) {
          System.out.println("Status : " + statuses.item(i).getTextContent());
        }

      }
    }
  }

  public static void main(String[] args) {
    // parseXMLString();
    parseXmlWithRepeatTags();

  }

}
