package main.java.api_calls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenericZillowAPICaller {

  private String request;
  private String response;
  private Logger logger = LogManager.getLogger(this.getClass().getName());

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public String getResponseFromLastCall() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public String makeApiCall() throws IOException {
    String results = Request.Get(this.request).execute().returnContent().toString();
    this.response = results;
    return this.response;
  }

  public String makeApiCall(String requestString) throws IOException {
    this.request = requestString;
    return this.makeApiCall();
  }

  /**
   * A Builder class to create Strings that can be used for calling the Zillow API's.
   * 
   * @author brandonbogan
   *
   */
  public static class ZillowRequestBuilder {
    private String baseUrl = "http://www.zillow.com/webservice/GetSearchResults.htm";
    private String request = "?";
    private String zwsid = "X1-ZWz19thqfce13f_3s95g";
    private HashMap<String, String> params = new HashMap<String, String>();
    private Logger logger = LogManager.getLogger(this.getClass().getName());

    public String getBaseUrl() {
      return baseUrl;
    }

    public ZillowRequestBuilder setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Sets the base url based on the location of the specified API
     * 
     * @param api The {@code ZillowAPI} to base the url on
     * @return This {@code ZillowRequestBuilder}, for chaining, with the updated base url
     */
    public ZillowRequestBuilder setBaseUrl(ZillowAPI api) {
      String url;
      switch (api) {
        case GetZestimate:
          url = "http://www.zillow.com/webservice/GetZestimate.htm";
          break;
        case GetSearchResults:
          url = "http://www.zillow.com/webservice/GetSearchResults.htm";
          break;
        case GetDeepSearchResults:
          url = "http://www.zillow.com/webservice/GetDeepSearchResults.htm";
          break;
        case GetComps:
          url = "http://www.zillow.com/webservice/GetComps.htm";
          break;
        case GetDeepComps:
          url = "http://www.zillow.com/webservice/GetDeepComps.htm";
          break;
        case GetUpdatedProperyDetails:
          url = "http://www.zillow.com/webservice/GetUpdatedPropertyDetails.htm";
          break;
        default:
          url = "http://www.zillow.com/webservice/GetZestimate.htm";
      }
      this.baseUrl = url;
      logger.trace("Given argument " + api + ", using base URL" + url);
      return this;
    }

    public String getRequest() {
      return request;
    }

    public ZillowRequestBuilder setRequest(String request) {
      this.request = request;
      return this;
    }

    public String getZwsid() {
      return zwsid;
    }

    public ZillowRequestBuilder setZwsid(String zwsid) {
      this.zwsid = zwsid;
      return this;
    }

    public HashMap<String, String> getParams() {
      return params;
    }

    public ZillowRequestBuilder setParams(HashMap<String, String> params) {
      this.params = params;
      return this;
    }

    /**
     * Adds the given parameters and their values to the existing map of parameters and values
     * 
     * @param paramsToAdd A {@code Map} of parameter names to parameter values. Can be of an
     *        arbitrary length.
     * @return This request builder, with an updated map of params and values
     */
    public ZillowRequestBuilder addParams(Map<String, String> paramsToAdd) {
      for (String newKey : paramsToAdd.keySet()) {
        String newValue = this.encodeString(paramsToAdd.get(newKey));
        newKey = this.encodeString(newKey);
        this.params.put(newKey, newValue);
        logger.trace("Including <param_name, param_value> = <" + newKey + ", " + newValue + " in API paramaters");
      }
      return this;
    }

    /**
     * Add or update a single parameter
     * 
     * @param paramName
     * @param paramValue
     * @return This ZillowRequestBuilder, for chaining.
     */
    public ZillowRequestBuilder setSingleParam(String paramName, String paramValue) {
      paramName = this.encodeString(paramName);
      paramValue = this.encodeString(paramValue);
      this.params.put(paramName, paramValue);
      logger.trace("Including <param_name, param_value> = <" + paramName + ", " + paramValue + " in API paramaters");
      return this;
    }

    /**
     * Builds the base url and the parameters that have been supplied into a String that can be used
     * to make API calls
     * 
     * @return A well-formatted, URL-encoded String containing the base URL, the ZWS-ID, and any
     *         parameters that have been provided to this ZillowRequestBuilder
     */
    public String build() {
      logger.trace("Building URL for API request...");
      StringBuilder sb = new StringBuilder(this.baseUrl).append("?");
      sb.append("zws-id=").append(zwsid);
      for (String param : this.params.keySet()) {
        sb.append("&").append(param).append("=").append(this.params.get(param));
      }
      String response = sb.toString();
      logger.trace("URL String built: %s", response);
      return response;
    }

    public String encodeString(String arg) {
      String response = arg;
      String charSet = "UTF-8";
      try {
        response = URLEncoder.encode(response, charSet);
        logger.trace("Successfully encoded %s to %s using %s", arg, response, charSet);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        logger.error("Error in encoding String: " + arg);
        logger.error("Error was an UnsupportedEncodingException: ",  e);
      }
      return response;
    }

  }

}
