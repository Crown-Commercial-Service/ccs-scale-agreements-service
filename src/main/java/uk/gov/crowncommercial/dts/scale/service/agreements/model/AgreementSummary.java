package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * Agreement Summary.
 */
@Data
public class AgreementSummary {

	/**
	 * Commercial Agreement Number, e.g. "RM1045".
	 */
	private String number;

	/**
	 * Commercial Agreement Name e.g. "Linen and Laundry Services".
	 */
	private String name;
}