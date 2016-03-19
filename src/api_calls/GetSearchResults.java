package api_calls;

public class GetSearchResults {
  private final String baseUrl;
  private String request;
  private String response;
  private String zpid;
  private String address;
  private String zipcode;

  public GetSearchResults() {
    this.baseUrl = "http://www.zillow.com/webservice/GetSearchResults.htm";
  }

}
