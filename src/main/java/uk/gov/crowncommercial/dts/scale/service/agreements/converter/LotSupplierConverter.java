package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Converts LotOrganisationRole from database into LotSupplier DTO
 */
@Component
public class LotSupplierConverter extends AbstractConverter<LotOrganisationRole, LotSupplier> {

  @Override
  protected LotSupplier convert(final LotOrganisationRole source) {

    final LotSupplier lotSupplier = new LotSupplier();
    final Organisation orgSource = source.getOrganisation();

    // Primary Org Identifier
    // TODO: Dedicated converter?
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

    final Set<ContactPointLotOrgRole> contactPointLogOrgRoles = source.getContactPointLotOrgRoles();

    // LotSupplier -> organization -> contactPoint (primary_ind = true)
    // LotSupplier -> organization -> address (primary_ind = true)
    // TODO: Catch NPE if primary is null
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
    lotSupplier.setOrganization(orgDTO);

    // LotSupplier -> supplierStatus
    // TODO: Dedicated converter
    Optional.ofNullable(orgSource.getStatus()).ifPresent(
        status -> lotSupplier.setSupplierStatus(SupplierStatus.valueOf(status.toUpperCase())));

    // LotSupplier -> lotContacts (Contact -> ContactPoint)
    lotSupplier.setLotContacts(contactPointLogOrgRoles.stream().map(cplor -> {
      final Contact contact = new Contact();
      contact.setLotContactReason(cplor.getContactPointReason().getName());
      contact.setContactPoint(convertFromContactPointLotOrgRole(cplor));
      return contact;
    }).collect(Collectors.toSet()));

    return lotSupplier;
  }

  /**
   * TODO: Dedicated converter?
   *
   * @param source
   * @return
   */
  static ContactPoint convertFromContactPointLotOrgRole(final ContactPointLotOrgRole source) {
    final ContactDetail contactDetail = source.getContactDetail();
    final ContactPoint contactPoint = new ContactPoint();
    contactPoint.setName(source.getContactPointName());
    contactPoint.setEmail(contactDetail.getEmailAddress());
    contactPoint.setTelephone(contactDetail.getTelephoneNumber());
    contactPoint.setFaxNumber(contactDetail.getFaxNumber());
    return contactPoint;
  }

  /**
   * TODO: Dedicated converter?
   *
   * @param source
   * @return
   */
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
