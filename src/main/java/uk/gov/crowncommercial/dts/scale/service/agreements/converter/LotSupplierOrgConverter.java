package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collections;
import java.util.Set;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organization;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.PartyRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Convert a DB {@link LotOrganisationRole} to an {@link Organization}
 */
@Component
@RequiredArgsConstructor
public class LotSupplierOrgConverter extends AbstractConverter<LotOrganisationRole, Organization> {

  private final ConverterUtils converterUtils;

  @Override
  protected Organization convert(final LotOrganisationRole source) {

    final Organisation org = source.getOrganisation();
    final Set<ContactPointLotOrgRole> contactPointLogOrgRoles = source.getContactPointLotOrgRoles();

    final Organization lotSupplier = new Organization();
    lotSupplier.setName(org.getLegalName());
    lotSupplier.setId(
        (org.getRegistryCode() == null ? "TODO" : org.getRegistryCode()) + "-" + org.getEntityId());
    lotSupplier.setIdentifier(converterUtils.convertOrgToOrgId(org));
    lotSupplier.setRoles(Collections.singleton(PartyRole.SUPPLIER));
    contactPointLogOrgRoles.stream().filter(ContactPointLotOrgRole::getPrimary).findFirst()
        .ifPresent(cplor -> {
          lotSupplier.setContactPoint(converterUtils.convertContactPointLotOrgRole(cplor));
          lotSupplier
              .setAddress(converterUtils.convertContactDetailToAddress(cplor.getContactDetail()));
        });

    lotSupplier.setDetails(converterUtils.convertOrgToOrgDetail(org));
    return lotSupplier;
  }

}
