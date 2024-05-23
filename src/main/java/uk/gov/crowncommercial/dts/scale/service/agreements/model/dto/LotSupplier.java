package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

/**
 * LotSupplier
 */
@Data
public class LotSupplier implements Serializable {

  private Organization organization;

  private SupplierStatus supplierStatus;

  private Set<Contact> lotContacts;

  private String lastUpdatedBy;
}
