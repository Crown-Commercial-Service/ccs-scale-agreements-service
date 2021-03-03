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

    // Organisation
    final Organization orgDTO = new Organization();

    orgDTO.setName(orgSource.getLegalName());
    orgDTO.setId(orgSource.getEntityId());
    orgDTO.setIdentifier(primaryIdentifier);
    orgDTO.setRoles(Collections.singleton(PartyRole.SUPPLIER));

    final Set<ContactPointLotOrgRole> contactPointLogOrgRoles = source.getContactPointLotOrgRoles();

    // TODO: Primary Contact Point (primary_ind = true)
    // TODO: Catch NPE if primary is null
    final Set<ContactPointLotOrgRole> primaryContactPointsLotOrgRole = contactPointLogOrgRoles
        .stream().filter(ContactPointLotOrgRole::getPrimary).collect(Collectors.toSet());

    // Primary ContactPoint Munging.. TODO: Extract / re-use for LotSupplier -> Organization ->
    // Address
    final ContactPoint primaryContactPointDTO = new ContactPoint();
    for (final ContactPointLotOrgRole primaryContactPointLotOrgRole : primaryContactPointsLotOrgRole) {
      final String virtualAddress =
          primaryContactPointLotOrgRole.getContactDetail().getVirtualAddress();
      switch (primaryContactPointLotOrgRole.getContactDetail().getContactMethodType().getName()) {
        case "Email":
          primaryContactPointDTO.setEmail(virtualAddress);
          break;
        case "Phone":
          primaryContactPointDTO.setTelephone(virtualAddress);
          break;
        case "Fax":
          primaryContactPointDTO.setFaxNumber(virtualAddress);
          break;
        // TODO: "name", "url" ?
      }
    }
    orgDTO.setContactPoint(primaryContactPointDTO);

    // Organization -> Address
    // TODO: Candidate for auto-mapping?
    final Optional<ContactPointLotOrgRole> optAddressContactPointsLotOrgRole =
        contactPointLogOrgRoles.stream()
            .filter(cplor -> "Postal Address"
                .equalsIgnoreCase(cplor.getContactDetail().getContactMethodType().getName()))
            .findFirst();
    if (optAddressContactPointsLotOrgRole.isPresent()) {
      final ContactDetail addressContactDetail =
          optAddressContactPointsLotOrgRole.get().getContactDetail();
      final Address address = new Address();
      address.setStreetAddress(addressContactDetail.getStreetAddress());
      address.setLocality(addressContactDetail.getLocality());
      address.setRegion(addressContactDetail.getRegion());
      address.setPostalCode(addressContactDetail.getPostalCode());
      address.setCountryName(addressContactDetail.getCountryCode());
      orgDTO.setAddress(address);
    }

    // Organization -> OrganizationDetail
    orgDTO.setDetails(convertFromDBOrg(orgSource));
    lotSupplier.setOrganization(orgDTO);

    // TODO: Dedicated converter / handle null
    lotSupplier.setSupplierStatus(SupplierStatus.valueOf(orgSource.getStatus().toUpperCase()));

    // LotSupplier -> lotContacts (Contact -> ContactPoint)
    for (final ContactPointLotOrgRole contactPointLotOrgRole : contactPointLogOrgRoles) {

      // Group by

    }

    return lotSupplier;
  }

  /**
   * TODO: Dedicated converter?
   *
   * @param orgSource
   * @return
   */
  private OrganizationDetail convertFromDBOrg(final Organisation orgSource) {

    final OrganizationDetail orgDetail = new OrganizationDetail();
    orgDetail.setCreationDate(orgSource.getIncorporationDate());
    orgDetail.setCountryCode(orgSource.getIncorporationCountry());
    orgDetail.setCompanyType(orgSource.getBusinessType());
    orgDetail.setIsSme(orgSource.getIsSme());
    orgDetail.setIsVcse(orgSource.getIsVcse());
    orgDetail.setStatus(orgSource.getStatus());
    orgDetail.setActive(orgSource.getIsActive());
    return orgDetail;
  }

}
