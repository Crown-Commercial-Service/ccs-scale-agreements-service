package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import java.util.Collection;

import lombok.Data;

/**
 * Lot Rule.
 */
@Data
public class LotRule {

	/**
	 * Unique identifier of the rule.
	 */
	private String ruleId;

	/**
	 * Name of the rule (3 or 4 word description).
	 */
	private String name;

	/**
	 * Attributes.
	 */
	private Collection<NameValueType> lotAttributes;

	/**
	 * Data required from the relevant transaction to be able to evaluate the rule.
	 */
	private Collection<TransactionData> transactionData;

	/**
	 * Evaluation Type.
	 */
	private EvaluationType evaluationType;
}
