package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ContactPoint;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;

/**
 * Common conversion utilities
 */
@Slf4j
@UtilityClass
public class ConverterUtils {

  /**
   * Delegates to {@link Enum#valueOf(Class, String)}, catching and logging any exception conditions
   * that occur and returning null in that case. Pre-processes non-blank string arguments by
   * converting to upper case and replacing char '-' with '_' throughout.
   *
   * @param <T>
   * @param enumClass
   * @param s
   * @return the enum instance or null
   */
  public static <T extends Enum<T>> T enumFromString(final Class<T> enumClass, final String s) {

    if (s == null || s.trim().length() == 0) {
      log.debug("String is null or empty and cannot be converted to enum type {}", enumClass);
      return null;
    }
    try {
      return Enum.valueOf(enumClass, s.toUpperCase().replace('-', '_'));
    } catch (final Exception e) {
      log.warn("String '{}' cannot be converted to enum type {}", s, enumClass);
      return null;
    }
  }

  /**
   * Converts a DB {@link ContactPointLotOrgRole} to a {@link ContactPoint}
   *
   * @param source
   * @return a contact point
   */
  public static ContactPoint convertFromContactPointLotOrgRole(
      final ContactPointLotOrgRole source) {
    final ContactDetail contactDetail = source.getContactDetail();
    final ContactPoint contactPoint = new ContactPoint();
    contactPoint.setName(source.getContactPointName());
    contactPoint.setEmail(contactDetail.getEmailAddress());
    contactPoint.setTelephone(contactDetail.getTelephoneNumber());
    contactPoint.setFaxNumber(contactDetail.getFaxNumber());
    return contactPoint;
  }

}
