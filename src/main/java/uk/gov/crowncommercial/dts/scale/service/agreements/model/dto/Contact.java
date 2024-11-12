package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Contact
 */
@Data
public class Contact implements Serializable {

  @JsonProperty("contact")
  ContactPoint contactPoint;

  String contactId;

  String contactReason;

}
