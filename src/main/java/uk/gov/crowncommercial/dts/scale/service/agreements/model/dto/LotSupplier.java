package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.util.Set;
import lombok.Data;

/**
 * LotSupplier
 */
@Data
public class LotSupplier {

  private Organization organization;

  private SupplierStatus supplierStatus;

  private Set<Contact> lotContacts;
}
