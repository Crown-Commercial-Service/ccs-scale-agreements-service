package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.Constants;

import java.io.Serializable;

/**
 * Party role
 */
public enum PartyRole implements Serializable {

  @JsonProperty("buyer")
  BUYER,

  @JsonProperty("procuringEntity")
  PROCURING_ENTITY,

  @JsonProperty("supplier")
  SUPPLIER,

  @JsonProperty("tenderer")
  TENDERER,

  @JsonProperty("funder")
  FUNDER,

  @JsonProperty("enquirer")
  ENQUIRER,

  @JsonProperty("payer")
  PAYER,

  @JsonProperty("payee")
  PAYEE,

  @JsonProperty("reviewBody")
  REVIEW_BODY,

  @JsonProperty("interestedParty")
  INTERESTED_PARTY,

  @JsonProperty(Constants.OCDS_ROLE_FRAMEWORK_OWNER)
  FRAMEWORK_OWNER;

}
