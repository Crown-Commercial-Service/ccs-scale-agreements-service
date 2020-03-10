package uk.gov.crowncommercial.dts.scale.agreements.model;

import lombok.Data;

/**
 * Lot Summary.
 */
@Data
public class LotSummary {

	/**
	 * Lot number e.g. "1" for Lot 1.
	 */
	private String number;

	/**
	 * Lot name e.g. "Finance".
	 */
	private String name;
}
