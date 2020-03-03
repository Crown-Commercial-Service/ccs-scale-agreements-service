package uk.gov.crowncommercial.dts.scale.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Agreement.
 */
@Data
public class Agreement {

	@NonNull
	private Long id;

	@NonNull
	private String name;

}
