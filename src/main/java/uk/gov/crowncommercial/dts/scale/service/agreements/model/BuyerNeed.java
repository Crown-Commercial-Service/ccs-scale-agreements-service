package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * Buyer Need.
 */
@Data
public class BuyerNeed {

	/**
	 * Id of the need (to allow alignment with CaT).
	 */
	private Integer id;

	/**
	 * Name of Buyer Need.
	 */
	private String name;

	/**
	 * Text to be displayed for Buyer need.
	 */
	private String text;
}
