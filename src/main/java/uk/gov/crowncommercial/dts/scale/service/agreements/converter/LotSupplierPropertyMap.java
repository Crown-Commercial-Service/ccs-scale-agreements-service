package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSupplier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;

/**
 * Explicit mappings for the conversion of a {@link LotOrganisationRole} to {@link LotSupplier}
 */
@Component
@RequiredArgsConstructor
public class LotSupplierPropertyMap extends PropertyMap<LotOrganisationRole, LotSupplier> {

  private final LotSupplierOrgConverter lotSupplierOrgConverter;
  private final LotContactsConverter lotContactsConverter;
  private final SupplierStatusConverter supplierStatusConverter;

  @Override
  protected void configure() {
    using(lotSupplierOrgConverter).map(source, destination.getOrganization());
    using(lotContactsConverter).map(source.getContactPointLotOrgRoles(),
        destination.getLotContacts());
    using(supplierStatusConverter).map(source.getOrganisation().getStatus(),
        destination.getSupplierStatus());
  }

}
