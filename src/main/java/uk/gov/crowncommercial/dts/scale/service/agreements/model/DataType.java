package uk.gov.crowncommercial.dts.scale.service.agreements.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DataType {

	@JsonProperty("string")
	STRING,

	@JsonProperty("integer")
	INTEGER,

	@JsonProperty("number")
	NUMBER
}
