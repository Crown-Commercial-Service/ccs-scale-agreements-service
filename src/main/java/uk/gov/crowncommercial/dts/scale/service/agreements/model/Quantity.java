package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * Quantity.
 */
@Data
public class Quantity {

	/**
	 * The unit of the quantity value.
	 */
	private String unit;

	/**
	 * The number of units.
	 */
	private Double value;
}
