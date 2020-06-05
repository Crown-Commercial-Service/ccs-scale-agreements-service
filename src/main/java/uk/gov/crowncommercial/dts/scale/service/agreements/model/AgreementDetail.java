package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import java.util.Collection;
import java.util.Date;

import lombok.Data;

/**
 * Agreement Detail.
 */
@Data
public class AgreementDetail {

	/**
	 * Commercial Agreement Number e.g. "RM1045".
	 */
	private String number;

	/**
	 * Commercial Agreement Name e.g. "Technology Products 2".
	 */
	private String name;

	/**
	 * Short textual description of the commercial agreement.
	 */
	private String description;

	/**
	 * Effective start date of Commercial Agreement.
	 */
	private Date startDate;

	/**
	 * Effective end date of Commercial Agreement.
	 */
	private Date endDate;

	/**
	 * Effective start date of Commercial Agreement.
	 */
	private String detailUrl;

	/**
	 * Contact details.
	 */
	private Collection<ContactDetail> contactDetails;

	/**
	 * Short description of the benefit.
	 */
	private Collection<String> benefits;

	/**
	 * Associated lots.
	 */
	private Collection<LotSummary> lots;

	/**
	 * Associated documents.
	 */
	private Collection<Document> documents;
}
