package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * Contract Length.
 */
@Data
public class ContractLength {

	/**
	 * The unit of the length value.
	 */
	private String unit;

	/**
	 * The number of units.
	 */
	private Integer length;
}
