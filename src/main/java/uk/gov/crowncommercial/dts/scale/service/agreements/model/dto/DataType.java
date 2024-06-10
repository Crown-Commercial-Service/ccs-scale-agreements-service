package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum DataType {

  @JsonProperty("string")
  STRING("string"),

  @JsonProperty("integer")
  INTEGER("integer"),

  @JsonProperty("number")
  NUMBER("number"),

  @JsonProperty("date")
  DATE("date");

  private String name;

  private DataType(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static DataType getDataTypeFromName(String value) {

    for (DataType dt : DataType.values()) {
      if (dt.getName().equalsIgnoreCase(value)) {
        return dt;
      }
    }

    return null;
  }
}
