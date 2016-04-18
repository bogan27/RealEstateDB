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
import main.java.classes_for_db.Zestimate;

/**
 * @author brandonbogan
 *
 */
public class GetZestimateResultParser extends ParseZillowResultsAbstract {

  /**
   * @param data
   */
  public GetZestimateResultParser(String data) {
    super(data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see xml_parsers.ParseZillowResultsAbstract#parseData()
   */
  @Override
  public List<DbTableObject> parseData() {
    ArrayList<DbTableObject> zestimateList = new ArrayList<DbTableObject>();

    if (Integer.parseInt(this.getStatusCode()) == 0) {
      Document doc = this.getXmlDoc();
      NodeList resultSet = doc.getElementsByTagName("response");
      for (int i = 0; i < resultSet.getLength(); i++) {
        Zestimate z = new Zestimate();
        Node iResult = resultSet.item(i);
        if (iResult.getNodeType() == Node.ELEMENT_NODE) {
          Element zestimate = (Element) iResult;

          z.setZpid(this.extractFirstValue(zestimate, "zpid"));
          z.setZestimate(this.extractFirstValue(zestimate, "amount"));
          z.setLastUpdated(this.extractFirstValue(zestimate, "last-updated"));
          z.setThirtyDayChange(this.extractFirstValue(zestimate, "valueChange"));
          z.setPercentileValue(this.extractFirstValue(zestimate, "percentile"));

          Element valuationRange =
              (Element) zestimate.getElementsByTagName("valuationRange").item(0);

          z.setvaluationLow(this.extractFirstValue(valuationRange, "low"));
          z.setValuationHigh(this.extractFirstValue(valuationRange, "high"));

        }
        zestimateList.add(z);
      }
    }
    return zestimateList;
  }

}
