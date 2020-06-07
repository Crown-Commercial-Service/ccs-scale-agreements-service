package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Buying Methods.
 */
public enum BuyingMethod {

	@JsonProperty("DirectAward")
	DIRECT_AWARD,

	@JsonProperty("FurtherCompetition")
	FURTHER_COMPETITION,

	@JsonProperty("MarketPlace")
	MARKETPLACE,

	@JsonProperty("EAuction")
	E_AUCTION
}
