package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Supplier status
 */
public enum SupplierStatus {

  @JsonProperty("active")
  ACTIVE,

  @JsonProperty("suspended")
  SUSPENDED,

  @JsonProperty("excluded")
  EXCLUDED;

}
