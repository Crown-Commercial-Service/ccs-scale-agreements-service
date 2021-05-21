package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Optional;
import java.util.Set;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Address;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ContactPoint;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organization;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationIdentifier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointCommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Converts <code>Set&lt;CommercialAgreementOrgRole&gt;</code> from DB into an
 * <code>Organization</code>, based on the owner role (if present).
 */
@Component
public class AgreementOwnerConverter
    extends AbstractConverter<Set<CommercialAgreementOrgRole>, Organization> {

  @Override
  protected Organization convert(final Set<CommercialAgreementOrgRole> source) {

    Optional<CommercialAgreementOrgRole> optOrgRole = source.stream()
        .filter(c -> c.getRoleType() != null && c.getRoleType().getName().equalsIgnoreCase("owner"))
        .findFirst();

    if (optOrgRole.isPresent()) {
      CommercialAgreementOrgRole orgRole = optOrgRole.get();
      Organisation org = orgRole.getOrganisation();
      Organization owner = new Organization();

      // Contact Point
      final Optional<ContactPointCommercialAgreementOrgRole> optCPOrgRole =
          orgRole.getContactPointCommercialAgreementOrgRoles().stream().findFirst();

      if (optCPOrgRole.isPresent()) {
        ContactPointCommercialAgreementOrgRole cpOrgRole = optCPOrgRole.get();
        ContactDetail contactDetail = cpOrgRole.getContactDetail();

        // Contact Point
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setName(cpOrgRole.getContactPointName());
        contactPoint.setEmail(contactDetail.getEmailAddress());
        contactPoint.setTelephone(contactDetail.getTelephoneNumber());
        contactPoint.setFaxNumber(contactDetail.getFaxNumber());
        contactPoint.setUrl(contactDetail.getUrl());

        // Address
        Address address = new Address();
        address.setCountryCode(contactDetail.getCountryCode());
        address.setPostalCode(contactDetail.getPostalCode());
        address.setRegion(contactDetail.getRegion());
        address.setStreetAddress(contactDetail.getStreetAddress());
        address.setLocality(contactDetail.getLocality());
        // Country code is not in the DB. Trev has no plans to add it unless required
        // address.setCountryName(null);

        owner.setContactPoint(contactPoint);
        owner.setAddress(address);
      }

      // OrganizationIdentifier
      OrganizationIdentifier identifier = new OrganizationIdentifier();
      identifier.setLegalName(org.getLegalName());
      identifier.setUri(org.getUri());
      identifier.setId(org.getEntityId());
      // data needs to match the Scheme enum values (Trev is going to see if he can get this)
      // if so will also need a Scheme converter
      // identifier.setScheme(org.getRegistryCode());


      // OrganizationDetail
      OrganizationDetail detail = new OrganizationDetail();
      detail.setActive(org.getIsActive());
      detail.setIsVcse(org.getIsVcse());
      detail.setCreationDate(org.getIncorporationDate());
      detail.setCompanyType(org.getBusinessType());
      detail.setCountryCode(org.getIncorporationCountry());
      detail.setStatus(org.getStatus());
      detail.setScale(null);

      // Trev thinks this should be a concatenation of these values (but see registry_code/scheme
      // issue above)
      owner.setId((org.getRegistryCode() == null ? "TODO" : org.getRegistryCode()) + "-"
          + org.getEntityId());
      owner.setName(org.getLegalName());
      owner.setIdentifier(identifier);
      owner.setDetails(detail);

      // No additional identifier data available (confirmed by Trev)
      // owner.setAdditionalIdentifiers(null);

      // Role would be 'owner' - but that does not exist (waiting for confirmation from Dave that
      // this is ok)
      // owner.setRoles(null);
      // Note: Dave has added 'frameworkOwner' role to
      // https://raw.githubusercontent.com/Crown-Commercial-Service/ccs-api-definitions-common/main/CCS_OCDS_Standards/CCS-OCDS_CodeLists.yaml#/components/schemas/PartyRoles'
      // Think it should be this

      return owner;

    } else {
      return null;
    }

  }

}
