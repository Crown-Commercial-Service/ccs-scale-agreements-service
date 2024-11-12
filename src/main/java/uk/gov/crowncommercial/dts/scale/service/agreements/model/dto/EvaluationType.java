package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * How the rule should be evaluated - equal = rule is true if the lotAttribute is equal to the
 * transactionData greater = rule is true if the lotAttribute is greater than the transactionData
 * less = rule is true if the lotAttribute is less than the transactionData complex = rule specific
 * code is required to evaluate (typically where there are multiple variables) flag = rule is always
 * true. The presence of the rule is used to flag that certain behaviour is required. In some cases
 * data may be passed in the lotAttributes.
 * 
 */
public enum EvaluationType {

  @JsonProperty("equal")
  EQUAL("equal"),

  @JsonProperty("greater")
  GREATER("greater"),

  @JsonProperty("less")
  LESS("less"),

  @JsonProperty("complex")
  COMPLEX("complex"),

  @JsonProperty("flag")
  FLAG("flag");

  private String name;

  private EvaluationType(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static EvaluationType getEvaluationTypeFromName(String value) {

    for (EvaluationType et : EvaluationType.values()) {
      if (et.getName().equalsIgnoreCase(value)) {
        return et;
      }
    }

    return null;
  }
}
