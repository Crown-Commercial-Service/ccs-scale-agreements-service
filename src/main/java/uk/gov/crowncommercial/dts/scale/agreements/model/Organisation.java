package uk.gov.crowncommercial.dts.scale.agreements.model;

import lombok.Data;

/**
 * Organisation.
 */
@Data
public class Organisation {

	/**
	 * Conclave Organisation Id.
	 */
	private String id;

	/**
	 * Company name.
	 */
	private String name;

	/**
	 * Contact name for this context.
	 */
	private String contactName;

	/**
	 * Contact email for this context.
	 */
	private String contactEmail;

}
