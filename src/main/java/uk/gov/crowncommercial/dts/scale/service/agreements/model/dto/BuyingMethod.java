package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * Buying Methods.
 */
@RequiredArgsConstructor
public enum BuyingMethod {

  @JsonProperty("DirectAward")
  DIRECT_AWARD("DirectAward"),

  @JsonProperty("FurtherCompetition")
  FURTHER_COMPETITION("FurtherCompetition"),

  @JsonProperty("Marketplace")
  MARKETPLACE("Marketplace"),

  @JsonProperty("EAuction")
  E_AUCTION("EAuction"),

  @JsonProperty("NotSpecified")
  NONE("NotSpecified");

  private String name;

  private BuyingMethod(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static BuyingMethod getBuyingMethodFromName(String name) {
    for (BuyingMethod bm : values()) {
      if (bm.getName().equalsIgnoreCase(name)) {
        return bm;
      }
    }
    return null;
  }

  public static Collection<String> names() {
    return Arrays.stream(values()).map(BuyingMethod::getName).collect(Collectors.toList());
  }

}
