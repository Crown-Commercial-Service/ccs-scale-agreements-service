package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 */
@Data
public class Contact {

  @JsonProperty("contact")
  ContactPoint contactPoint;

  String contactId;

  String lotContactReason;

}
