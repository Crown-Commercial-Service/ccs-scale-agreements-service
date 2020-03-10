package uk.gov.crowncommercial.dts.scale.agreements.model;

import java.util.Date;

import lombok.Data;

/**
 * Agreement Update.
 */
@Data
public class AgreementUpdate {

	/**
	 * Date that the update was added.
	 */
	private Date date;

	/**
	 * Link to further information regarding the update.
	 */
	private String url;

	/**
	 * Actual update text.
	 */
	private String text;

}
