package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import java.util.Collection;

import lombok.Data;

/**
 * Lot Detail.
 */
@Data
public class LotDetail {

	/**
	 * Lot number e.g. "1" for Lot 1.
	 */
	private String number;

	/**
	 * Lot name e.g. "Finance".
	 */
	private String name;

	/**
	 * Short textual description of the Lot.
	 */
	private String description;

	/**
	 * Type of the lot.
	 */
	private LotType type;

	/**
	 * Service offered by lot.
	 */
	private Collection<Service> services;

	/**
	 * Products offered by lot.
	 */
	private Collection<Product> products;

	/**
	 * Suppliers in lot.
	 */
	private Collection<Organisation> suppliers;

	/**
	 * Routes to Market in lot.
	 */
	private Collection<RouteToMarket> routesToMarket;

	/**
	 * A sector permitted to buy using the Agreement.
	 */
	private Collection<String> sectors;

	/**
	 * Terms & conditions.
	 */
	private Document termsAndConditions;

	/**
	 * Associated documents.
	 */
	private Collection<Document> documents;

	/**
	 * Buyer needs.
	 */
	private Collection<BuyerNeed> buyerNeeds;

	/**
	 * Rules.
	 */
	private Collection<LotRule> rules;

}
