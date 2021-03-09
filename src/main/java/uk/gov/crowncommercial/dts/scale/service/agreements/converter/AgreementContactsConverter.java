package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Contact;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ContactPoint;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;

/**
 * Converts <code>Set&lt;CommercialAgreementOrgRole&gt;</code> from DB into
 * <code>Collection&lt;Contact&gt;</code>
 */
@Component
public class AgreementContactsConverter
    extends AbstractConverter<Set<CommercialAgreementOrgRole>, Collection<Contact>> {

  @Override
  protected Set<Contact> convert(final Set<CommercialAgreementOrgRole> source) {

    return source.stream()
        .flatMap(caor -> caor.getContactPointCommercialAgreementOrgRoles().stream()).map(cpcaor -> {
          final ContactDetail contactDetail = cpcaor.getContactDetail();
          final Contact contact = new Contact();
          final ContactPoint contactPoint = new ContactPoint();
          contactPoint.setName(cpcaor.getContactPointName());
          contactPoint.setEmail(contactDetail.getEmailAddress());
          contactPoint.setTelephone(contactDetail.getTelephoneNumber());
          contactPoint.setFaxNumber(contactDetail.getFaxNumber());
          contactPoint.setUrl(contactDetail.getUrl());
          contact.setContactReason(cpcaor.getContactPointReason().getName());
          contact.setContactPoint(contactPoint);
          return contact;
        }).collect(Collectors.toSet());
  }

}
