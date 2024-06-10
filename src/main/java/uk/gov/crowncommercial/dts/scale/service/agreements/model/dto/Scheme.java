package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * List or register from which an organization identifier is taken
 */
public enum Scheme {

  @JsonProperty("GB-COH")
  GBCOH("GB-COH"),

  @JsonProperty("GB-MPR")
  GBMPR("GB-MPR"),

  @JsonProperty("GB-NIC")
  GBNIC("GB-NIC"),

  @JsonProperty("GB-CHC")
  GBCHC("GB-CHC"),

  @JsonProperty("GB-SC")
  GBSC("GB-SC"),

  @JsonProperty("GB-WALEDU")
  GBWALEDU("GB-WALEDU"),

  @JsonProperty("GB-SCOTEDU")
  GBSCOTEDU("GB-SCOTEDU"),

  @JsonProperty("GB-GOR")
  GBGOR("GB-GOR"),

  @JsonProperty("GB-LANI")
  GBLANI("GB-LANI"),

  @JsonProperty("GB-NHS")
  GBNHS("GB-NHS"),

  @JsonProperty("GB-SRS")
  GBSRS("GB-SRS"),

  @JsonProperty("US-DUNS")
  USDUNS("US-DUNS");

  private String name;

  private Scheme(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static Scheme getSchemeFromName(String value) {

    for (Scheme scheme : Scheme.values()) {
      if (scheme.getName().equalsIgnoreCase(value)) {
        return scheme;
      }
    }

    return null;
  }

}