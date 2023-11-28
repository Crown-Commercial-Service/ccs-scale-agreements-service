package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Supplier status
 */
public enum SupplierStatus {

  @JsonProperty("active")
  ACTIVE("active"),

  @JsonProperty("suspended")
  SUSPENDED("suspended"),

  @JsonProperty("excluded")
  EXCLUDED("excluded");

  private String name;

  private SupplierStatus(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static SupplierStatus getSupplierStatusFromName(String value) {

    for (SupplierStatus ss : SupplierStatus.values()) {
      if (ss.getName().equalsIgnoreCase(value)) {
        return ss;
      }
    }

    return null;
  }

}