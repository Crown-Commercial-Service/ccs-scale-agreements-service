package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.Constants;

import java.io.Serializable;

/**
 * Party role
 */
public enum PartyRole implements Serializable {

  @JsonProperty("buyer")
  BUYER("buyer"),

  @JsonProperty("procuringEntity")
  PROCURING_ENTITY("procuringEntity"),

  @JsonProperty("supplier")
  SUPPLIER("supplier"),

  @JsonProperty("tenderer")
  TENDERER("tenderer"),

  @JsonProperty("funder")
  FUNDER("funder"),

  @JsonProperty("enquirer")
  ENQUIRER("enquirer"),

  @JsonProperty("payer")
  PAYER("payer"),

  @JsonProperty("payee")
  PAYEE("payee"),

  @JsonProperty("reviewBody")
  REVIEW_BODY("reviewBody"),

  @JsonProperty("interestedParty")
  INTERESTED_PARTY("interestedParty"),

  @JsonProperty(Constants.OCDS_ROLE_FRAMEWORK_OWNER)
  FRAMEWORK_OWNER("OCDS_ROLE_FRAMEWORK_OWNER");

  private String name;

  private PartyRole(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @JsonCreator
  public static PartyRole getPartyRoleFromName(String value) {

    for (PartyRole pr : PartyRole.values()) {
      if (pr.getName().equals(value)) {
        return pr;
      }
    }
    return null;
  }

}