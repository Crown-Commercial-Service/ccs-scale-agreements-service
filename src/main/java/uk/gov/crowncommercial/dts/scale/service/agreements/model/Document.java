package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import lombok.Data;

/**
 * Document.
 */
@Data
public class Document {

	/**
	 * URI for the document. Should be a perma-link and can be used as a key for the
	 * document.
	 */
	private String url;

	/**
	 * Document name.
	 */
	private String name;

	/**
	 * string Version number of the document - does not prescribe the format
	 */
	private String version;

	/**
	 * The type of document e.g. overview, t&cs, guidance, how to buy, contract
	 * notice etc.
	 */
	private String documentType;
}
