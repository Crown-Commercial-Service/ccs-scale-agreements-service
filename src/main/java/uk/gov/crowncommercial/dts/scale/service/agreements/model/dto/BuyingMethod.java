package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * Buying Methods.
 */
@RequiredArgsConstructor
public enum BuyingMethod {

  DIRECT_AWARD("DirectAward"),

  FURTHER_COMPETITION("FurtherCompetition"),

  MARKETPLACE("MarketPlace"),

  E_AUCTION("EAuction");

  private final String name;

  @JsonValue
  public String getName() {
    return name;
  }

  public static BuyingMethod fromName(String name) {
    for (BuyingMethod buyingMethod : values()) {
      if (buyingMethod.name.equals(name)) {
        return buyingMethod;
      }
    }
    return null;
  }

  public static Collection<String> names() {
    return Arrays.stream(values()).map(BuyingMethod::getName).collect(Collectors.toSet());
  }

}
