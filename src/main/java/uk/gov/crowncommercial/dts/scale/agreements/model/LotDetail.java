package uk.gov.crowncommercial.dts.scale.agreements.model;

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
	 * The minimum value for which this Lot applies.
	 */
	private Double minimumValue;

	/**
	 * The maximum value for which this Lot applies.
	 */
	private Double maximumValue;

	/**
	 * PLACEHOLDER. Needs to be an anyOf for National:Regional or individual Regions
	 * or even lowest level NUTS2
	 */
	private String location;

	/**
	 * Minimum contract length.
	 */
	private ContractLength minContractLength;

	/**
	 * Maximum contract length.
	 */
	private ContractLength maxContractLength;

	/**
	 * Minimum quantity.
	 */
	private Quantity minQuantity;

	/**
	 * Maximum quantity.
	 */
	private Quantity maxQuantity;

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
	 * Available buying methods.
	 */
	private BuyingMethods buyingMethods;

	/**
	 * The URL to use for the system to progress the procurement (e.g. BaT, CaT,
	 * other).
	 */
	private String buyingSystemUrl;

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

}
