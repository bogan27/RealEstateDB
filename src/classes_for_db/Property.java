package classes_for_db;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dbConnectors.DBWriter;

public class Property implements DbTableObject {
  private BigInteger zpid;
  private String streetAddress;
  private int zipCode;
  private String city;
  private String state;
  private float latitude;
  private float longitude;
  private int countyCode;
  private String useCode;
  private int yearBuilt;
  private int lotSizeSqFt;
  private int finishedSqFt;
  private float bathroomCount;
  private int bedroomCount;
  private Date lastSoldDate;
  private int lastSoldPrice;

  private Zestimate zestimate;
  private boolean zestimateUsed;

  private Neighborhood region;
  private boolean regionUsed;

  private List<TaxAssessment> assessmentList;
  private boolean taxAssessmentUsed;
  private TaxAssessment recentAssessment;

  private PropertyDetails details;
  private boolean detailsUsed;

  private List<ZillowComparable> compList;
  private boolean compsUsed;
  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

  public Property() {
    this.zestimate = new Zestimate();
    this.region = new Neighborhood();
    this.recentAssessment = new TaxAssessment();
    this.assessmentList = new ArrayList<TaxAssessment>();
    this.assessmentList.add(this.recentAssessment);
    this.details = new PropertyDetails();
    this.compList = new ArrayList<ZillowComparable>();
  }


  public BigInteger getZpid() {
    return zpid;
  }


  /**
   * 
   * @param zpid A BigInteger representing the zpid of the property. This argument needs to be a
   *        {@code BigInteger} instead of an {@code int} because the size of some of the larger
   *        zpids exceeds the max value of an int.
   * @throws IllegalArgumentException if {@code zpid} is less than 0 or greater than 10000000000 (10
   *         billion).
   */
  public void setZpid(BigInteger zpid) {
    BigInteger min = new BigInteger("0");
    BigInteger max = new BigInteger("10000000000");
    if (zpid.compareTo(min) < 0) {
      throw new IllegalArgumentException(
          "zpid cannot be negative. Argument given for zpid: " + zpid);
    }
    if (zpid.compareTo(max) >= 0) {
      throw new IllegalArgumentException(
          "Argument given for zpid exceeds maximum allowed value of 10000000000 (10 billion). Argument given: "
              + zpid);
    } else {
      this.zpid = zpid;
      this.zestimate.setZpid(zpid);
      this.recentAssessment.setZpid(zpid);
      this.details.setZpid(zpid);
    }
  }

  /**
   * 
   * @param zpid A String representation of this property's zpid
   * @throws IllegalArgumentException if {@code zpid} is longer than 11 characters, is less than 0,
   *         or is greater than 10000000000 (10 billion).
   */
  public void setZpid(String zpid) {
    if (zpid.length() > 11) {
      throw new IllegalArgumentException(
          "Argument for zpid exceeds maximum allowable length of 11 characters. Argument given: "
              + zpid);
    } else {
      BigInteger id = new BigInteger(zpid);
      this.setZpid(id);
    }
  }


  public String getStreetAddress() {
    return streetAddress;
  }


  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }


  public int getZipCode() {
    return zipCode;
  }


  /**
   * 
   * @param zipCode
   * @throws IllegalArgumentException if {@code zipCode} does not fall within the acceptable range
   *         of 0 <= zipCode <= 99999.
   */
  public void setZipCode(int zipCode) {
    if (zipCode < 0 | zipCode > 99999) {
      throw new IllegalArgumentException(
          "Argument for zipCode does not fall withing the acceptable range of 0 <= zipCode <= 99999. Argument given: "
              + zipCode);
    } else {
      this.zipCode = zipCode;
    }
  }

  /**
   * 
   * @param zipCode
   * @throws IllegalArgumentException if {@code zipCode} is longer than 5 characters or does not
   *         fall within the acceptable range of 0 <= zipCode <= 99999.
   */
  public void setZipCode(String zipCode) {
    if (zipCode.length() > 5) {
      throw new IllegalArgumentException(
          "Argument for zipCode exceeds the maximum length allowed of 5 characters. Argument given: "
              + zipCode);
    } else {
      this.setZipCode(Integer.parseInt(zipCode));
    }
  }


  public String getCity() {
    return city;
  }


  public void setCity(String city) {
    this.city = city;
  }


  public String getState() {
    return state;
  }


  public void setState(String state) {
    this.state = state;
  }


  public float getLatitude() {
    return latitude;
  }


  /**
   * 
   * @param latitude
   * @throws IlleglArgumentException if {@code latitude} is less than -180 or greater than 180.
   */
  public void setLatitude(float latitude) {
    if (Math.abs(latitude) > 180) {
      throw new IllegalArgumentException(
          "Argument for latitude falls outside of the allowed range of -180 <= latitude <= 180. Argument given: "
              + latitude);
    }
    this.latitude = latitude;
  }

  /**
   * 
   * @param latitude
   * @throws IlleglArgumentException if {@code latitude} is less than -180 or greater than 180.
   */
  public void setlatitude(String latitude) {
    float l = Float.parseFloat(latitude);
    this.setLatitude(l);
  }


  public float getLongitude() {
    return longitude;
  }


  /**
   * 
   * @param longitude
   * @throws IlleglArgumentException if {@code longitude} is less than -180 or greater than 180.
   */
  public void setLongitude(float longitude) {
    if (Math.abs(longitude) > 180) {
      throw new IllegalArgumentException(
          "Argument for longitude falls outside of the allowed range of -180 <= longitude <= 180. Argument given: "
              + longitude);
    }
    this.longitude = longitude;
  }

  /**
   * 
   * @param longitude
   * @throws IlleglArgumentException if {@code longitude} is less than -180 or greater than 180.
   */
  public void setLongitude(String longitude) {
    float l = Float.parseFloat(longitude);
    this.setLongitude(l);
  }

  public int getCountyCode() {
    return countyCode;
  }


  /**
   * 
   * @param countyCode
   * @throws IllegalArgumentException if {@code countyCode} is not within the range of 0 <
   *         countyCode < 1000000.
   */
  public void setCountyCode(int countyCode) {
    if (countyCode < 0 | countyCode >= 1000000) {
      throw new IllegalArgumentException(
          "Argument for countyCode did not fall within the range of 0 < countyCode < 1000000. Argument evaluated to: "
              + countyCode);
    } else {
      this.countyCode = countyCode;
    }
  }


  /**
   * 
   * @param countyCode A String representation of the countyCode of the property
   * @throws IllegalArgumentException if {@code countyCode} is longer than 5 characters, or not
   *         within the range of 0 < countyCode < 1000000.
   */
  public void setCountyCode(String countyCode) {
    if (countyCode.length() > 5) {
      throw new IllegalArgumentException(
          "Argument for countyCode exceeds the max allowed length of 5 characters. Argument given: "
              + countyCode);
    } else if (countyCode.length() > 0) {
      int code = Integer.parseInt(countyCode);
      this.setCountyCode(code);
    }
  }


  public String getUseCode() {
    return useCode;
  }


  public void setUseCode(String useCode) {
    this.useCode = useCode;
  }


  public int getYearBuilt() {
    return yearBuilt;
  }


  /**
   * 
   * @param yearBuilt
   * @throws IllegalArgumentException if {@code yearBuilt} is greater than 2017 or less than 0.
   */
  public void setYearBuilt(int yearBuilt) {
    if (yearBuilt < 0 | yearBuilt > 2017) {
      throw new IllegalArgumentException("Invalid year given: " + yearBuilt);
    }
    this.yearBuilt = yearBuilt;
  }


  /**
   * 
   * @param yearBuilt A String representation of the year the property was built.
   * @throws IllegalArgumentException if {@code yearBuilt} exceeds 4 characters in length, is
   *         greater than 2017, or less than 0.
   */
  public void setYearBuilt(String yearBuilt) {
    if (yearBuilt.length() > 4) {
      throw new IllegalArgumentException(
          "Argument for yearBuilt exceed max allowed length of 4. Argument given: " + yearBuilt);
    } else if (yearBuilt.length() > 0) {
      int yr = Integer.parseInt(yearBuilt);
      if (yr < 0) {
        throw new IllegalArgumentException(
            "Argument for year built cannot be negative. Argument evaluated to: " + yr);
      }
      if (yr > 2017) {
        throw new IllegalArgumentException(
            "Year built cannot be greater than 2017. Argument given evaluated to: " + yr);
      } else {
        this.yearBuilt = yr;
      }
    }
  }


  public int getLotSizeSqFt() {
    return lotSizeSqFt;
  }


  /**
   * 
   * @param lotSizeSqFt
   * @throws IllegalArgumentException if {@code lotSizeSqFt} is >= 1000000000 or negative.
   */
  public void setLotSizeSqFt(int lotSizeSqFt) {
    if (lotSizeSqFt >= 1000000000) {
      throw new IllegalArgumentException(
          "Argument for lotSizeSqFt must be less than 1 billion. Argument given: " + lotSizeSqFt);
    }
    if (lotSizeSqFt < 0) {
      throw new IllegalArgumentException(
          "Argument for lotSizeSqFt cannot be negative. Argument given: " + lotSizeSqFt);
    } else {
      this.lotSizeSqFt = lotSizeSqFt;
    }
  }

  /**
   * 
   * @param lotSizeSqFt A String representation of the number of square feet on the lot of this
   *        property
   * @throws IllegalArgumentException if {@code lotSizeSqFt} is longer than 9 character or
   *         represents a negative int.
   */
  public void setLotSizeSqFt(String lotSizeSqFt) {
    if (lotSizeSqFt.length() > 9) {
      throw new IllegalArgumentException(
          "Argument for lotSizeSqFt cannot exceed 9 characters in length. Argument given: "
              + lotSizeSqFt);
    } else {
      int sqFt = Integer.parseInt(lotSizeSqFt);
      if (sqFt < 0) {
        throw new IllegalArgumentException(
            "Lot Size sq ft cannot be negative. Invalid value given: " + lotSizeSqFt);
      } else {
        this.lotSizeSqFt = sqFt;
      }
    }
  }


  public int getFinishedSqFt() {
    return finishedSqFt;
  }


  /**
   * 
   * @param finishedSqFt
   * @throws IllegalArgumentException if {@code finishedSqFt} is greater than or equal to 1 billion,
   *         or is negative.
   */
  public void setFinishedSqFt(int finishedSqFt) {
    if (finishedSqFt >= 1000000000) {
      throw new IllegalArgumentException(
          "Argument for finishedSqFt must be less than 1 billion. Argument given: " + finishedSqFt);
    }
    if (finishedSqFt < 0) {
      throw new IllegalArgumentException(
          "Argument for finishedSqFt cannot be negative. Argument given: " + finishedSqFt);
    } else {
      this.finishedSqFt = finishedSqFt;
    }
  }

  /**
   * 
   * @param finishedSqFtString A String representation of the number of finished square feet the
   *        property has.
   * @throws IllegalArgumentException if the length of {@code finishedSqFtString) is greater than 9.
   */
  public void setFinishedSqFt(String finishedSqFtString) {
    if (finishedSqFtString.length() > 9) {
      throw new IllegalArgumentException(
          "Argument finishedSqFt cannot be longer than 9 characters. Argument given: "
              + finishedSqFt);
    } else if (finishedSqFtString.length() > 0) {
      int sqFt = Integer.parseInt(finishedSqFtString);
      if (sqFt < 0) {
        throw new IllegalArgumentException(
            "Finished sq ft cannot be negative. Invalid value given: " + finishedSqFt);
      } else {
        this.finishedSqFt = sqFt;
      }
    }
  }


  public float getBathroomCount() {
    return bathroomCount;
  }

  /**
   * 
   * @param bathroomCount The number of bathrooms the property has. Can have one decimal place, for
   *        half baths.
   * @throws IllegalArgumentException if {@code bathroomCount} is greater than or equal to 100, or
   *         if it is negative.
   */
  public void setBathroomCount(float bathroomCount) {
    if (bathroomCount >= 100) {
      throw new IllegalArgumentException(
          "Whoa! That's way too many bathrooms! Argument for number of bathrooms must be less than 100.");
    }
    if (bathroomCount < 0) {
      throw new IllegalArgumentException(
          "Cannot have a negative number of bathrooms. Argument given: " + bathroomCount);
    }
    this.bathroomCount = bathroomCount;
  }

  /**
   * 
   * @param bathroomCountString A String representation of the number of bathrooms the property has.
   * @throws IllegalArgumentException if {@code bathrromCountString} is longer than 4 character,
   *         greater than 100, or less than 0.
   */
  public void setBathroomCount(String bathroomCountString) {
    if (bathroomCountString.length() > 5) {
      throw new IllegalArgumentException(
          "Too many characters in argument. Max of 4 characters allowed. Argument given: "
              + bathroomCountString + " and zpid: " + this.zpid);
    } else if (bathroomCountString.length() > 0) {
      float bathroomCount = Float.parseFloat(bathroomCountString);
      if (bathroomCount >= 1000) {
        throw new IllegalArgumentException(
            "Argument for bathroomCountString exceeds max number of bathrooms allowed. Argument must be less than 1000.");
      }
      if (bathroomCount < 0) {
        throw new IllegalArgumentException(
            "Cannot have a negative number of bathrooms. Argument given: " + bathroomCount);
      } else {
        this.bathroomCount = bathroomCount;
      }
    } else {
      this.bathroomCount = 0;
    }
  }


  public int getBedroomCount() {
    return bedroomCount;
  }


  /**
   * 
   * @param bedroomCountString A String representation on an integer count of the number of
   *        bedrooms, which will get converted to an int.
   * 
   * @throws IllegalArgumentException If {@code bedroomCountString} is longer than 2 characters.
   */
  public void setBedroomCount(String bedroomCountString) {
    if (bedroomCountString.length() > 3) {
      throw new IllegalArgumentException(
          "Length of argument bedroomCountString exceeds max allowed length of 2.");
    } else if (bedroomCountString.length() > 0) {
      int bedroomCount = Integer.parseInt(bedroomCountString);
      this.bedroomCount = bedroomCount;
    } else {
      this.bedroomCount = 0;
    }

  }

  public void setBedroomCount(int count) {
    if (count >= 1000) {
      throw new IllegalArgumentException(
          "Whoa! That's way too many bedrooms! Argument for number of bedrooms must be less than 100, got " +count);
    }
    if (count < 0) {
      throw new IllegalArgumentException(
          "Cannot have a negative number of bedrooms. Argument given: " + count);
    }
    this.bedroomCount = count;
  }

  public Date getLastSoldDate() {
    return this.lastSoldDate;
  }

  public java.sql.Date getLastSoldDateSql() {

    StringBuilder sb = new StringBuilder();

    sb.append(this.lastSoldDate.getYear() + "-");
    sb.append(this.lastSoldDate.getMonth() + "-" + this.lastSoldDate.getDay());
    String dateString = sb.toString();
    java.sql.Date convertedDate = java.sql.Date.valueOf(dateString);
    return convertedDate;
  }

  public String getLastSoldDateString() {
    String result = "";
    if (this.lastSoldDate != null) {
      result = this.sdf.format(this.lastSoldDate);
    }
    return result;
  }

  public void setLastSoldDate(Date lastSoldDate) {
    this.lastSoldDate = lastSoldDate;
  }

  /**
   * 
   * @param lastSoldDate A String representation of the date the property was last sold. This String
   *        will be converted to a Date.
   */
  public void setLastSoldDate(String lastSoldDate) {
    if (lastSoldDate != null && lastSoldDate.length() > 0) {
      try {
        Date date = this.sdf.parse(lastSoldDate);
        this.lastSoldDate = date;
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    }
  }


  public int getLastSoldPrice() {
    return lastSoldPrice;
  }


  /**
   * 
   * @param lastSoldPrice A String representation of the price the property was last sold for. This
   *        String will be converted and stored as an int.
   * @throws IllegalArgumentException if {@code lastSoldPrice} is longer than 8 characters ($100
   *         million dollars or more).
   */
  public void setLastSoldPrice(String lastSoldPrice) {
    if (lastSoldPrice.length() > 8) {
      throw new IllegalArgumentException("Length of lastSoldPrice exceeds max length allowed (8)");
    } else if (lastSoldPrice.length() > 0) {
      int lastPriceAsInt = Integer.parseInt(lastSoldPrice);
      this.lastSoldPrice = lastPriceAsInt;
    }
  }

  public void setLastSoldPrice(int price) {
    if (price >= 1000000000) {
      throw new IllegalArgumentException("Last sold price exceeds 1b - that's too much!");
    } else {
      this.lastSoldPrice = price;
    }
  }


  ////////////////////////
  /// Delegate methods ///
  ////////////////////////
  /**
   * @return
   * @see classes_for_db.Zestimate#getZestimate()
   */
  public int getZestimateAmount() {
    return zestimate.getZestimate();
  }


  /**
   * @param zestimate
   * @see classes_for_db.Zestimate#setZestimate(int)
   */
  public void setZestimateAmount(int zestimate) {
    this.zestimateUsed = true;
    this.zestimate.setZestimate(zestimate);
  }


  /**
   * @param zestimate
   * @see classes_for_db.Zestimate#setZestimate(java.lang.String)
   */
  public void setZestimate(String zestimate) {
    this.zestimateUsed = true;
    this.zestimate.setZestimate(zestimate);
  }


  /**
   * @return
   * @see classes_for_db.Zestimate#getLastUpdated()
   */
  public Date getLastUpdated() {
    return zestimate.getLastUpdated();
  }

  /**
   * @return
   * @see classes_for_db.Zestimate#getLastUpdated()
   */
  public String getLastUpdatedString() {
    return zestimate.getLastUpdatedString();
  }

  /**
   * @param lastUpdated
   * @see classes_for_db.Zestimate#setLastUpdated(java.lang.String)
   */
  public void setLastUpdated(String lastUpdated) {
    this.zestimateUsed = true;
    zestimate.setLastUpdated(lastUpdated);
  }


  /**
   * @return
   * @see classes_for_db.Zestimate#getThirtyDayChange()
   */
  public int getThirtyDayChange() {
    return zestimate.getThirtyDayChange();
  }


  /**
   * @param thirtyDayChange
   * @see classes_for_db.Zestimate#setThirtyDayChange(int)
   */
  public void setThirtyDayChange(int thirtyDayChange) {
    this.zestimateUsed = true;
    zestimate.setThirtyDayChange(thirtyDayChange);
  }


  /**
   * @param thirtyDayChange
   * @see classes_for_db.Zestimate#setThirtyDayChange(java.lang.String)
   */
  public void setThirtyDayChange(String thirtyDayChange) {
    this.zestimateUsed = true;
    zestimate.setThirtyDayChange(thirtyDayChange);
  }


  /**
   * @return
   * @see classes_for_db.Zestimate#getValuationHigh()
   */
  public int getValuationHigh() {
    return zestimate.getValuationHigh();
  }


  /**
   * @param valuationHigh
   * @see classes_for_db.Zestimate#setValuationHigh(int)
   */
  public void setValuationHigh(int valuationHigh) {
    this.zestimateUsed = true;
    zestimate.setValuationHigh(valuationHigh);
  }


  /**
   * @param valuationHigh
   * @see classes_for_db.Zestimate#setValuationHigh(java.lang.String)
   */
  public void setValuationHigh(String valuationHigh) {
    this.zestimateUsed = true;
    zestimate.setValuationHigh(valuationHigh);
  }


  /**
   * @return
   * @see classes_for_db.Zestimate#getvaluationLow()
   */
  public int getvaluationLow() {
    return zestimate.getvaluationLow();
  }


  /**
   * @param valuationLow
   * @see classes_for_db.Zestimate#setvaluationLow(int)
   */
  public void setvaluationLow(int valuationLow) {
    this.zestimateUsed = true;
    zestimate.setvaluationLow(valuationLow);
  }


  /**
   * @param valuationLow
   * @see classes_for_db.Zestimate#setvaluationLow(java.lang.String)
   */
  public void setvaluationLow(String valuationLow) {
    this.zestimateUsed = true;
    zestimate.setvaluationLow(valuationLow);
  }


  /**
   * @return
   * @see classes_for_db.Zestimate#getPercentileValue()
   */
  public float getPercentileValue() {
    return zestimate.getPercentileValue();
  }


  /**
   * @param percentileValue
   * @see classes_for_db.Zestimate#setPercentileValue(float)
   */
  public void setPercentileValue(float percentileValue) {
    this.zestimateUsed = true;
    zestimate.setPercentileValue(percentileValue);
  }


  /**
   * @param percentileValue
   * @see classes_for_db.Zestimate#setPercentileValue(java.lang.String)
   */
  public void setPercentileValue(String percentileValue) {
    this.zestimateUsed = true;
    zestimate.setPercentileValue(percentileValue);
  }


  /**
   * @param rentZestimate
   * @see classes_for_db.Zestimate#setRentZestimate(int)
   */
  public void setRentZestimate(int rentZestimate) {
    zestimate.setRentZestimate(rentZestimate);
  }


  /**
   * @param rentZestimate
   * @see classes_for_db.Zestimate#setRentZestimate(java.lang.String)
   */
  public void setRentZestimate(String rentZestimate) {
    zestimate.setRentZestimate(rentZestimate);
  }


  /**
   * @param rentThirtyDayChange
   * @see classes_for_db.Zestimate#setRentThirtyDayChange(int)
   */
  public void setRentThirtyDayChange(int rentThirtyDayChange) {
    zestimate.setRentThirtyDayChange(rentThirtyDayChange);
  }


  /**
   * @param rentThirtyDayChange
   * @see classes_for_db.Zestimate#setRentThirtyDayChange(java.lang.String)
   */
  public void setRentThirtyDayChange(String rentThirtyDayChange) {
    zestimate.setRentThirtyDayChange(rentThirtyDayChange);
  }


  /**
   * @param minRent
   * @see classes_for_db.Zestimate#setMinRent(int)
   */
  public void setMinRent(int minRent) {
    zestimate.setMinRent(minRent);
  }


  /**
   * @param minRent
   * @see classes_for_db.Zestimate#setMinRent(java.lang.String)
   */
  public void setMinRent(String minRent) {
    zestimate.setMinRent(minRent);
  }


  /**
   * @param maxRent
   * @see classes_for_db.Zestimate#setMaxRent(int)
   */
  public void setMaxRent(int maxRent) {
    zestimate.setMaxRent(maxRent);
  }


  /**
   * @param maxRent
   * @see classes_for_db.Zestimate#setMaxRent(java.lang.String)
   */
  public void setMaxRent(String maxRent) {
    zestimate.setMaxRent(maxRent);
  }


  /**
   * @return
   * @see classes_for_db.Neighborhood#getRegionID()
   */
  public int getRegionID() {
    return region.getRegionID();
  }


  /**
   * @param regionID
   * @see classes_for_db.Neighborhood#setRegionID(int)
   */
  public void setRegionID(int regionID) {
    this.regionUsed = true;
    region.setRegionID(regionID);
  }


  /**
   * @param regionID
   * @see classes_for_db.Neighborhood#setRegionID(java.lang.String)
   */
  public void setRegionID(String regionID) {
    this.regionUsed = true;
    region.setRegionID(regionID);
  }


  /**
   * @return
   * @see classes_for_db.Neighborhood#getName()
   */
  public String getRegionName() {
    return region.getName();
  }


  /**
   * @param name
   * @see classes_for_db.Neighborhood#setName(java.lang.String)
   */
  public void setNameRegion(String name) {
    this.regionUsed = true;
    region.setName(name);
  }


  /**
   * @return
   * @see classes_for_db.Neighborhood#getzIndexChange()
   */
  public float getzIndexChange() {
    return region.getzIndexChange();
  }


  /**
   * @param zIndexChange
   * @see classes_for_db.Neighborhood#setzIndexChange(float)
   */
  public void setzIndexChange(float zIndexChange) {
    this.regionUsed = true;
    region.setzIndexChange(zIndexChange);
  }


  /**
   * @param zIndexChange
   * @see classes_for_db.Neighborhood#setzIndexChange(java.lang.String)
   */
  public void setzIndexChange(String zIndexChange) {
    this.regionUsed = true;
    region.setzIndexChange(zIndexChange);
  }


  /**
   * @return
   * @see classes_for_db.Neighborhood#getType()
   */
  public String getRegionType() {
    return region.getType();
  }


  /**
   * @param type
   * @see classes_for_db.Neighborhood#setType(java.lang.String)
   */
  public void setRegionType(String type) {
    this.regionUsed = true;
    region.setType(type);
  }


  /**
   * @return
   * @see classes_for_db.TaxAssessment#getTaxYear()
   */
  public int getTaxYear() {
    return recentAssessment.getTaxYear();
  }


  /**
   * @param taxYear
   * @see classes_for_db.TaxAssessment#setTaxYear(int)
   */
  public void setTaxYear(int taxYear) {
    this.taxAssessmentUsed = true;
    recentAssessment.setTaxYear(taxYear);
  }


  /**
   * @param taxYear
   * @see classes_for_db.TaxAssessment#setTaxYear(java.lang.String)
   */
  public void setTaxYear(String taxYear) {
    this.taxAssessmentUsed = true;
    recentAssessment.setTaxYear(taxYear);
  }


  /**
   * @return
   * @see classes_for_db.TaxAssessment#getTaxAssessment()
   */
  public float getTaxAssessmentAmount() {
    return recentAssessment.getTaxAssessment();
  }


  /**
   * @param taxAssessment
   * @see classes_for_db.TaxAssessment#setTaxAssessment(float)
   */
  public void setTaxAssessmentAmount(float taxAssessment) {
    this.taxAssessmentUsed = true;
    this.recentAssessment.setTaxAssessment(taxAssessment);
  }

  /**
   * @param taxAssessment
   * @see classes_for_db.TaxAssessment#setTaxAssessment(String)
   */
  public void setTaxAssessment(String taxAssessment) {
    this.taxAssessmentUsed = true;
    this.recentAssessment.setTaxAssessment(taxAssessment);
  }

  /**
   * @return
   * @see classes_for_db.PropertyDetails#getStatus()
   */
  public String getStatus() {
    return details.getStatus();
  }


  /**
   * @param status
   * @see classes_for_db.PropertyDetails#setStatus(java.lang.String)
   */
  public void setStatus(String status) {
    this.detailsUsed = true;
    details.setStatus(status);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getPosting_type()
   */
  public String getPosting_type() {
    return details.getPosting_type();
  }


  /**
   * @param posting_type
   * @see classes_for_db.PropertyDetails#setPosting_type(java.lang.String)
   */
  public void setPosting_type(String posting_type) {
    this.detailsUsed = true;
    details.setPosting_type(posting_type);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getLastUpdatedString()
   */
  public Date getLastUpdatedDetailsString() {
    return details.getLastUpdatedString();
  }


  /**
   * @param lastUpdated
   * @see classes_for_db.PropertyDetails#setLastUpdated(java.util.Date)
   */
  public void setLastUpdatedDetails(Date lastUpdated) {
    this.detailsUsed = true;
    details.setLastUpdated(lastUpdated);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getyearUpdated()
   */
  public int getyearUpdated() {
    return details.getyearUpdated();
  }


  /**
   * @param yearUpdated
   * @see classes_for_db.PropertyDetails#setyearUpdated(int)
   */
  public void setyearUpdated(int yearUpdated) {
    this.detailsUsed = true;
    details.setyearUpdated(yearUpdated);
  }


  /**
   * @param yearUpdated
   * @see classes_for_db.PropertyDetails#setyearUpdated(java.lang.String)
   */
  public void setyearUpdated(String yearUpdated) {
    this.detailsUsed = true;
    details.setyearUpdated(yearUpdated);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getNumFloors()
   */
  public int getNumFloors() {
    return details.getNumFloors();
  }


  /**
   * @param numFloors
   * @see classes_for_db.PropertyDetails#setNumFloors(int)
   */
  public void setNumFloors(int numFloors) {
    this.detailsUsed = true;
    details.setNumFloors(numFloors);
  }


  /**
   * @param numFloors
   * @see classes_for_db.PropertyDetails#setNumFloors(java.lang.String)
   */
  public void setNumFloors(String numFloors) {
    this.detailsUsed = true;
    details.setNumFloors(numFloors);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getBasement()
   */
  public String getBasement() {
    return details.getBasement();
  }


  /**
   * @param basement
   * @see classes_for_db.PropertyDetails#setBasement(java.lang.String)
   */
  public void setBasement(String basement) {
    this.detailsUsed = true;
    details.setBasement(basement);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getRoofType()
   */
  public String getRoofType() {
    return details.getRoofType();
  }


  /**
   * @param roofType
   * @see classes_for_db.PropertyDetails#setRoofType(java.lang.String)
   */
  public void setRoofType(String roofType) {
    this.detailsUsed = true;
    details.setRoofType(roofType);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getParkingType()
   */
  public String getParkingType() {
    return details.getParkingType();
  }


  /**
   * @param parkingType
   * @see classes_for_db.PropertyDetails#setParkingType(java.lang.String)
   */
  public void setParkingType(String parkingType) {
    this.detailsUsed = true;
    details.setParkingType(parkingType);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getRooms()
   */
  public String getRooms() {
    return details.getRooms();
  }


  /**
   * @param rooms
   * @see classes_for_db.PropertyDetails#setRooms(java.lang.String)
   */
  public void setRooms(String rooms) {
    this.detailsUsed = true;
    details.setRooms(rooms);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getNumRooms()
   */
  public int getNumRooms() {
    return details.getNumRooms();
  }


  /**
   * @param numRooms
   * @see classes_for_db.PropertyDetails#setNumRooms(int)
   */
  public void setNumRooms(int numRooms) {
    this.detailsUsed = true;
    details.setNumRooms(numRooms);
  }


  /**
   * @param numRooms
   * @see classes_for_db.PropertyDetails#setNumRooms(java.lang.String)
   */
  public void setNumRooms(String numRooms) {
    this.detailsUsed = true;
    details.setNumRooms(numRooms);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getHomeDescription()
   */
  public String getHomeDescription() {
    return details.getHomeDescription();
  }


  /**
   * @param homeDescription
   * @see classes_for_db.PropertyDetails#setHomeDescription(java.lang.String)
   */
  public void setHomeDescription(String homeDescription) {
    this.detailsUsed = true;
    details.setHomeDescription(homeDescription);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getNeighborhoodName()
   */
  public String getNeighborhoodName() {
    return details.getNeighborhoodName();
  }


  /**
   * @param neighborhoodName
   * @see classes_for_db.PropertyDetails#setNeighborhoodName(java.lang.String)
   */
  public void setNeighborhoodName(String neighborhoodName) {
    this.detailsUsed = true;
    details.setNeighborhoodName(neighborhoodName);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getSchoolDistrict()
   */
  public String getSchoolDistrict() {
    return details.getSchoolDistrict();
  }


  /**
   * @param schoolDistrict
   * @see classes_for_db.PropertyDetails#setSchoolDistrict(java.lang.String)
   */
  public void setSchoolDistrict(String schoolDistrict) {
    this.detailsUsed = true;
    details.setSchoolDistrict(schoolDistrict);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getPageViewThisMonth()
   */
  public int getPageViewThisMonth() {
    return details.getPageViewThisMonth();
  }


  /**
   * @param pageViewThisMonth
   * @see classes_for_db.PropertyDetails#setPageViewThisMonth(int)
   */
  public void setPageViewThisMonth(int pageViewThisMonth) {
    this.detailsUsed = true;
    details.setPageViewThisMonth(pageViewThisMonth);
  }


  /**
   * @param pageViewThisMonth
   * @see classes_for_db.PropertyDetails#setPageViewThisMonth(java.lang.String)
   */
  public void setPageViewThisMonth(String pageViewThisMonth) {
    this.detailsUsed = true;
    details.setPageViewThisMonth(pageViewThisMonth);
  }


  /**
   * @return
   * @see classes_for_db.PropertyDetails#getPageViewsTotal()
   */
  public int getPageViewsTotal() {
    return details.getPageViewsTotal();
  }


  /**
   * @param pageViewsTotal
   * @see classes_for_db.PropertyDetails#setPageViewsTotal(int)
   */
  public void setPageViewsTotal(int pageViewsTotal) {
    this.detailsUsed = true;
    details.setPageViewsTotal(pageViewsTotal);
  }


  /**
   * @param pageViewsTotal
   * @see classes_for_db.PropertyDetails#setPageViewsTotal(java.lang.String)
   */
  public void setPageViewsTotal(String pageViewsTotal) {
    this.detailsUsed = true;
    details.setPageViewsTotal(pageViewsTotal);
  }

  public PropertyDetails getPropertyDetails() {
    return details;
  }

  public void setPropertyDetails(PropertyDetails pd) {
    this.details = pd;
  }

  public Neighborhood getNeighborhood() {
    return region;
  }

  public void setNeighborhood(Neighborhood n) {
    this.region = n;
  }

  public Zestimate getZestimate() {
    return zestimate;
  }

  public void setZestimate(Zestimate z) {
    this.zestimate = z;
  }

  public List<TaxAssessment> getTaxAssessment() {

    return assessmentList;
  }

  public void setAssessmentList(List<TaxAssessment> lt) {
    this.taxAssessmentUsed = true;
    this.assessmentList = lt;
  }

  public TaxAssessment getRecentAssessment() {
    return this.assessmentList.get(0);
  }

  public void addAssessment(TaxAssessment ta) {
    this.taxAssessmentUsed = true;
    this.assessmentList.add(ta);
  }

  public List<ZillowComparable> getZillowComps() {
    return compList;
  }

  public void setZillowComps(List<ZillowComparable> lz) {
    this.compsUsed = true;
    this.compList = lz;
  }

  public void addComp(ZillowComparable zc) {
    this.compsUsed = true;
    this.compList.add(zc);
  }

  public ZillowComparable getBestComp() {
    this.compsUsed = true;
    return this.compList.get(0);
  }

  public List<ZillowComparable> getBest5Comps() {
    this.compsUsed = true;
    List<ZillowComparable> best5 = new ArrayList<ZillowComparable>();
    if (this.compList.size() > 5) {
      for (int i = 0; i < 5; i++) {
        best5.add(compList.get(i));
      }
      return best5;
    } else {
      return this.compList;
    }
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ZPID: " + this.getZpid() + "\n");
    sb.append("Zip code: " + this.getZipCode() + "\n");
    sb.append("Street Address: " + this.getStreetAddress() + "\n");
    sb.append("City: " + this.getCity() + "\n");
    sb.append("State: " + this.getState() + "\n");
    sb.append("Latitude: " + this.getLatitude() + "\n");
    sb.append("Longitude: " + this.getLongitude() + "\n");
    sb.append("FIPS County Code: " + this.getCountyCode() + "\n");
    sb.append("Use Code: " + this.getUseCode() + "\n");
    sb.append("Tax Assessment Year: " + this.getRecentAssessment().getTaxYear() + "\n");
    sb.append("Tax Assessment: " + this.getRecentAssessment().getTaxAssessment() + "\n");
    sb.append("Year Built: " + this.getYearBuilt() + "\n");
    sb.append("Finished square feet: " + this.getFinishedSqFt() + "\n");
    sb.append("Bathrooms: " + this.getBathroomCount() + "\n");
    sb.append("Bedrooms: " + this.getBedroomCount() + "\n");
    sb.append("Last sold date: " + this.getLastSoldDateString() + "\n");
    sb.append("Las sold price: " + this.getLastSoldPrice() + "\n");
    sb.append("Zestimate: " + this.getZestimate() + "\n");
    sb.append("Last updated: " + this.getLastUpdatedString() + "\n");
    sb.append("High Valuation: " + this.getValuationHigh() + "\n");
    sb.append("Low valuation: " + this.getvaluationLow() + "\n");
    sb.append("30 day change: " + this.getThirtyDayChange() + "\n");
    sb.append("Percentile Value: " + this.getPercentileValue() + "\n");
    sb.append("Region name: " + this.getRegionName() + "\n");
    sb.append("Region ID: " + this.getRegionID() + "\n");
    sb.append("Region Type: " + this.getRegionType() + "\n");
    sb.append("\n\n");
    return sb.toString();
  }


  @Override
  public List<DbTableObject> getDelegateObjects() {
    List<DbTableObject> response = new ArrayList<DbTableObject>();
    if (this.zestimateUsed) {
      response.add(this.zestimate);
    }
    if (this.taxAssessmentUsed) {
      for (TaxAssessment t : assessmentList) {
        response.add(t);
      }
    }
    if (this.regionUsed) {
      response.add(this.region);
    }
    if (this.detailsUsed) {
      response.add(this.details);
    }
    if (this.compsUsed) {
      for (ZillowComparable c : compList) {
        response.add(c);
      }
    }
    return response;
  }


  @Override
  public boolean writeToDB(DBWriter writer) {

    writer.insertObject(this);

    if (this.zestimateUsed) {
      this.zestimate.writeToDB(writer);
    }
    if (this.regionUsed) {
      this.region.writeToDB(writer);
    }
    if (this.detailsUsed) {
      this.details.writeToDB(writer);
    }
    if (this.taxAssessmentUsed) {
      this.recentAssessment.writeToDB(writer);
    }
    if (this.compsUsed) {
      for (ZillowComparable comp : this.compList) {
        comp.writeToDB(writer);
      }
    }
    return true;
  }
}
