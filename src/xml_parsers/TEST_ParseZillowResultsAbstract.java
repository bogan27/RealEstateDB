package xml_parsers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TEST_ParseZillowResultsAbstract {

  @SuppressWarnings("unused")
  private Document getXMLDoc() {
    String text = this.getXMLString();

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

  private String getXMLString() {
    String filename =
        "/Users/brandonbogan/Desktop/Schoolwork/Artifical Intelligence/Workspace/RealEstate/data/sampleHomeData.xml";
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(new File(filename)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("Error in reading in XML file.");
    }

    String line;
    StringBuilder sb = new StringBuilder();

    try {
      while ((line = br.readLine()) != null) {
        sb.append(line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error building string...");
    }
    return sb.toString();
  }
  
//  private ParseZillowResultsAbstract getParser(){
//    return new ParseZillowResultsAbstract(this.getXMLString());
//  }

  @Test
  public void testGetStatusCode() {
    
  }

  @Test
  public void testGetMessageText() {
    fail("Not yet implemented");
  }

  @Test
  public void testConvertStringToDoc() {
    fail("Not yet implemented");
  }

}
