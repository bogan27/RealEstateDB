package main.java.xml_parsers;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.classes_for_db.DbTableObject;

public class SearchResultParser extends ParseZillowResultsAbstract {

  public SearchResultParser(String data) throws SAXException, IOException {
    super(data);
  }

  @Override
  public ArrayList<DbTableObject> parseData() {

    ArrayList<DbTableObject> propertyList = new ArrayList<DbTableObject>();

    if (Integer.parseInt(this.getStatusCode()) == 0) {
      Document doc = this.getXmlDoc();
      NodeList resultSet = doc.getElementsByTagName("result");
      for (int i = 0; i < resultSet.getLength(); i++) {
        Node iResult = resultSet.item(i);
        if (iResult.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) iResult;
          propertyList.add(this.convertElementToProperty(element));
        }
      }
    }
    return propertyList;
  }
}
