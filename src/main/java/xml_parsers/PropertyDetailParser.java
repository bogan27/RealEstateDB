/**
 * 
 */
package main.java.xml_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.classes_for_db.DbTableObject;
import main.java.classes_for_db.PropertyDetails;

/**
 * @author brandonbogan
 *
 */
public class PropertyDetailParser extends ParseZillowResultsAbstract {

  /**
   * @param data
   * @throws IOException 
   * @throws SAXException 
   */
  public PropertyDetailParser(String data) throws SAXException, IOException {
    super(data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see xml_parsers.ParseZillowResultsAbstract#parseData()
   */
  @Override
  public List<DbTableObject> parseData() {
    ArrayList<DbTableObject> details = new ArrayList<DbTableObject>();

    if (Integer.parseInt(this.getStatusCode()) == 0) {
      Document doc = this.getXmlDoc();
      Element response = (Element) doc.getElementsByTagName("response").item(0);
      PropertyDetails detail = new PropertyDetails();
      detail.setZpid(this.extractFirstValue(response, "zpid"));
      detail.setPageViewThisMonth(this.extractFirstValue(response, "currentMonth"));
      detail.setPageViewsTotal(this.extractFirstValue(response, "total"));
      detail.setStatus(this.extractFirstValue(response, "status"));
      detail.setPosting_type(this.extractFirstValue(response, "type"));
      detail.setLastUpdated(this.extractFirstValue(response, "lastUpdatedDate"));
      detail.setyearUpdated(this.extractFirstValue(response, "yearUpdated"));
      detail.setNumFloors(this.extractFirstValue(response, "numFloors"));
      detail.setBasement(this.extractFirstValue(response, "basement"));
      detail.setRoofType(this.extractFirstValue(response, "roof"));
      detail.setParkingType(this.extractFirstValue(response, "parkingType"));
      detail.setRooms(this.extractFirstValue(response, "rooms"));
      detail.setNumRooms(this.extractFirstValue(response, "numRooms"));
      detail.setHomeDescription(this.extractFirstValue(response, "homeDescription"));
      detail.setNeighborhoodName(this.extractFirstValue(response, "neighborhood"));
      detail.setSchoolDistrict(this.extractFirstValue(response, "schoolDistrict"));
      
      details.add(detail);
    }
    return details;
  }

}
