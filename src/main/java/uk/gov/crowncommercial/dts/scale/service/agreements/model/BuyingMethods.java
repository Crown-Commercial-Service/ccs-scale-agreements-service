package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * Buying Methods.
 */
@Data
public class BuyingMethods {

	/**
	 * Does the Lot allow Direct Award?
	 */
	private boolean allowsDirectAward;

	/**
	 * Does the Lot allow Further Competition?
	 */
	private boolean allowsFurtherCompetition;

	/**
	 * Is the Lot a product based Marketplace?
	 */
	private boolean allowsMarketplace;

	/**
	 * Is the lot for eAuctions?
	 */
	private boolean allowsEAuction;
}
