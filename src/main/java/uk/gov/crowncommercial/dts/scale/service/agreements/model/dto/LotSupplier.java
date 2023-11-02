package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * LotSupplier
 */
@Data
public class LotSupplier implements Serializable {

  private Organization organization;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private SupplierStatus supplierStatus;

  private Set<Contact> lotContacts;
}
