package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static uk.gov.crowncommercial.dts.scale.service.agreements.config.Constants.OCDS_ROLE_FRAMEWORK_OWNER;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ContactPoint;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organization;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.PartyRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointCommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Converts <code>Set&lt;CommercialAgreementOrgRole&gt;</code> from DB into an
 * <code>Organization</code>, based on the owner role (if present).
 */
@Component
@RequiredArgsConstructor
public class AgreementOwnerConverter
    extends AbstractConverter<Set<CommercialAgreementOrgRole>, Organization> {

  private final ConverterUtils converterUtils;

  @Override
  protected Organization convert(final Set<CommercialAgreementOrgRole> source) {

    Optional<CommercialAgreementOrgRole> optOrgRole =
        source.stream()
            .filter(c -> c.getRoleType() != null
                && Objects.equals(OCDS_ROLE_FRAMEWORK_OWNER, c.getRoleType().getName()))
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

        owner.setContactPoint(contactPoint);
        owner.setAddress(converterUtils.convertContactDetailToAddress(contactDetail));
      }
      owner.setId(converterUtils.buildOrgId(org.getRegistryCode(), org.getEntityId()));
      owner.setName(org.getLegalName());
      owner.setIdentifier(converterUtils.convertOrgToOrgId(org));
      owner.setDetails(converterUtils.convertOrgToOrgDetail(org));
      owner.setRoles(Collections.singleton(PartyRole.FRAMEWORK_OWNER));
      return owner;

    } else {
      return null;
    }
  }

}
