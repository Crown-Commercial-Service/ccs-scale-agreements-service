package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.Conditions;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 *
 */
@Component
public class LotSupplierPropertyMap extends PropertyMap<LotOrganisationRole, LotSupplier> {

  @Override
  protected void configure() {
    using(ctx -> convertOrganisation(((LotOrganisationRole) ctx.getSource()).getOrganisation(),
        ((LotOrganisationRole) ctx.getSource()).getContactPointLotOrgRoles())).map(source,
            destination.getOrganization());

    using(ctx -> convertContacts(
        ((LotOrganisationRole) ctx.getSource()).getContactPointLotOrgRoles())).map(source,
            destination.getLotContacts());

    when(Conditions.isNotNull())
        .using(ctx -> SupplierStatus.valueOf(((String) ctx.getSource()).toUpperCase()))
        .map(source.getOrganisation().getStatus(), destination.getSupplierStatus());
  }

  static Set<Contact> convertContacts(final Set<ContactPointLotOrgRole> contactPointLogOrgRoles) {
    return contactPointLogOrgRoles.stream().map(cplor -> {
      final Contact contact = new Contact();
      contact.setContactReason(cplor.getContactPointReason().getName());
      contact.setContactPoint(convertFromContactPointLotOrgRole(cplor));
      return contact;
    }).collect(Collectors.toSet());
  }

  static Organization convertOrganisation(final Organisation orgSource,
      final Set<ContactPointLotOrgRole> contactPointLogOrgRoles) {

    final OrganizationIdentifier primaryIdentifier = new OrganizationIdentifier();
    primaryIdentifier.setId(orgSource.getEntityId());
    primaryIdentifier.setLegalName(orgSource.getLegalName());
    primaryIdentifier.setUri(orgSource.getUri());

    // TODO Missing from DB
    primaryIdentifier.setScheme(Scheme.GB_COH);

    // LotSupplier -> organization
    final Organization orgDTO = new Organization();
    orgDTO.setName(orgSource.getLegalName());
    orgDTO.setId(orgSource.getEntityId());
    orgDTO.setIdentifier(primaryIdentifier);
    orgDTO.setRoles(Collections.singleton(PartyRole.SUPPLIER));
    contactPointLogOrgRoles.stream().filter(ContactPointLotOrgRole::getPrimary).findFirst()
        .ifPresent(cplor -> {
          final ContactDetail contactDetail = cplor.getContactDetail();
          final Address address = new Address();
          address.setStreetAddress(contactDetail.getStreetAddress());
          address.setLocality(contactDetail.getLocality());
          address.setRegion(contactDetail.getRegion());
          address.setPostalCode(contactDetail.getPostalCode());
          address.setCountryName(contactDetail.getCountryCode());
          orgDTO.setContactPoint(convertFromContactPointLotOrgRole(cplor));
          orgDTO.setAddress(address);
        });

    // LotSupplier -> organization -> organizationDetail
    orgDTO.setDetails(convertFromDBOrg(orgSource));

    return orgDTO;
  }

  static ContactPoint convertFromContactPointLotOrgRole(final ContactPointLotOrgRole source) {
    final ContactDetail contactDetail = source.getContactDetail();
    final ContactPoint contactPoint = new ContactPoint();
    contactPoint.setName(source.getContactPointName());
    contactPoint.setEmail(contactDetail.getEmailAddress());
    contactPoint.setTelephone(contactDetail.getTelephoneNumber());
    contactPoint.setFaxNumber(contactDetail.getFaxNumber());
    return contactPoint;
  }

  static OrganizationDetail convertFromDBOrg(final Organisation source) {

    final OrganizationDetail orgDetail = new OrganizationDetail();
    orgDetail.setCreationDate(source.getIncorporationDate());
    orgDetail.setCountryCode(source.getIncorporationCountry());
    orgDetail.setCompanyType(source.getBusinessType());
    orgDetail.setIsSme(source.getIsSme());
    orgDetail.setIsVcse(source.getIsVcse());
    orgDetail.setStatus(source.getStatus());
    orgDetail.setActive(source.getIsActive());
    return orgDetail;
  }

}
