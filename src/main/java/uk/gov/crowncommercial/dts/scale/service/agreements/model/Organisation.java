package uk.gov.crowncommercial.dts.scale.service.agreements.model;

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
	 * Company registered name.
	 */
	private String name;

	/**
	 * Company trading name for this Lot
	 */
	private String tradingName;

	/**
	 * Contact name for this context.
	 */
	private String contactName;

	/**
	 * Contact email for this context.
	 */
	private String contactEmail;

}
