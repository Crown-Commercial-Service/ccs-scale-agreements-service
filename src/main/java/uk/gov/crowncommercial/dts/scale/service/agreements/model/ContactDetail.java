package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * 
 * Details of a contact. Initially email only, will be extended in future as
 * additional contact channels are added (e.g. phone number)
 */
@Data
public class ContactDetail {

	/**
	 * Email address.
	 */
	private String email;

	/**
	 * The 'type' of the contact in business terms, used to identify the correct
	 * contact for a particular purpose
	 */
	private String type;
}
