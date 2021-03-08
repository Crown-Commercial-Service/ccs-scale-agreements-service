package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Contact;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;

/**
 * Converts a DB set of {@link ContactPointLotOrgRole} to a set of {@link Contact}s
 */
@Component
public class LotContactsConverter
    extends AbstractConverter<Set<ContactPointLotOrgRole>, Set<Contact>> {

  @Override
  protected Set<Contact> convert(final Set<ContactPointLotOrgRole> source) {
    return source.stream().map(cplor -> {
      final Contact contact = new Contact();
      contact.setContactReason(cplor.getContactPointReason().getName());
      contact.setContactPoint(ConverterUtils.convertFromContactPointLotOrgRole(cplor));
      return contact;
    }).collect(Collectors.toSet());
  }

}
