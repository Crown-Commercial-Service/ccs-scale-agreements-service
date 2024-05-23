package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LotType.
 */
public enum LotType {

  @JsonProperty("product")
  PRODUCT("product"),

  @JsonProperty("service")
  SERVICE("service"),

  @JsonProperty("product and service")
  PRODUCT_AND_SERVICE("product and service");

  private String name;

  private LotType(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static LotType getLotTypeFromName(String value) {

    for (LotType lt : LotType.values()) {
      if (lt.getName().equalsIgnoreCase(value)) {
        return lt;
      }
    }
    return null;
  }
}
