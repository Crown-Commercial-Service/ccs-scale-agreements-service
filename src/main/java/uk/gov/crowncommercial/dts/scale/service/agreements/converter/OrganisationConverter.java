package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collections;
import java.util.Set;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Convert a DB {@link LotOrganisationRole} to an {@link Organization}
 */
@Component
public class OrganisationConverter extends AbstractConverter<LotOrganisationRole, Organization> {

  @Override
  protected Organization convert(final LotOrganisationRole source) {

    final Organisation dbOrg = source.getOrganisation();
    final Set<ContactPointLotOrgRole> contactPointLogOrgRoles = source.getContactPointLotOrgRoles();

    final OrganizationIdentifier orgIdentifier = new OrganizationIdentifier();
    orgIdentifier.setId(dbOrg.getEntityId());
    orgIdentifier.setLegalName(dbOrg.getLegalName());
    orgIdentifier.setUri(dbOrg.getUri());
    orgIdentifier.setScheme(ConverterUtils.enumFromString(Scheme.class, dbOrg.getRegistryCode()));

    final Organization organization = new Organization();
    organization.setName(dbOrg.getLegalName());
    organization.setId(dbOrg.getEntityId());
    organization.setIdentifier(orgIdentifier);
    organization.setRoles(Collections.singleton(PartyRole.SUPPLIER));
    contactPointLogOrgRoles.stream().filter(ContactPointLotOrgRole::getPrimary).findFirst()
        .ifPresent(cplor -> {
          final ContactDetail contactDetail = cplor.getContactDetail();
          final Address address = new Address();
          address.setStreetAddress(contactDetail.getStreetAddress());
          address.setLocality(contactDetail.getLocality());
          address.setRegion(contactDetail.getRegion());
          address.setPostalCode(contactDetail.getPostalCode());
          address.setCountryName(contactDetail.getCountryCode());
          organization.setContactPoint(ConverterUtils.convertFromContactPointLotOrgRole(cplor));
          organization.setAddress(address);
        });

    organization.setDetails(convertFromDBOrg(dbOrg));
    return organization;
  }

  static OrganizationDetail convertFromDBOrg(final Organisation dbOrg) {

    final OrganizationDetail orgDetail = new OrganizationDetail();
    orgDetail.setCreationDate(dbOrg.getIncorporationDate());
    orgDetail.setCountryCode(dbOrg.getIncorporationCountry());
    orgDetail.setCompanyType(dbOrg.getBusinessType());
    orgDetail.setIsSme(dbOrg.getIsSme());
    orgDetail.setIsVcse(dbOrg.getIsVcse());
    orgDetail.setStatus(dbOrg.getStatus());
    orgDetail.setActive(dbOrg.getIsActive());
    return orgDetail;
  }

}
