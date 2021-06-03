package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Common conversion utilities
 */
@Slf4j
@Component
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
  public <T extends Enum<T>> T enumFromString(final Class<T> enumClass, final String s) {

    if (s == null || s.trim().length() == 0) {
      log.debug("String is null or empty and cannot be converted to enum type {}", enumClass);
      return null;
    }
    try {
      return Enum.valueOf(enumClass, camelToSnakeCase(s).toUpperCase().replace('-', '_'));
    } catch (final Exception e) {
      log.warn("String '{}' cannot be converted to enum type {}", s, enumClass);
      return null;
    }
  }

  public static String camelToSnakeCase(String str) {
    String regex = "([a-z])([A-Z]+)";
    String replacement = "$1_$2";
    return str.replaceAll(regex, replacement);
  }

  /**
   * Converts a DB {@link ContactPointLotOrgRole} to a {@link ContactPoint}
   *
   * @param source
   * @return a contact point
   */
  public ContactPoint convertContactPointLotOrgRole(final ContactPointLotOrgRole source) {
    final ContactDetail contactDetail = source.getContactDetail();
    final ContactPoint contactPoint = new ContactPoint();
    contactPoint.setName(source.getContactPointName());
    contactPoint.setEmail(contactDetail.getEmailAddress());
    contactPoint.setTelephone(contactDetail.getTelephoneNumber());
    contactPoint.setFaxNumber(contactDetail.getFaxNumber());
    contactPoint.setUrl(contactDetail.getUrl());
    return contactPoint;
  }

  /**
   * Extracts the detail from a DB {@link Organisation} into an {@link OrganizationDetail}
   *
   * @param org
   * @return organization detail
   */
  public OrganizationDetail convertOrgToOrgDetail(final Organisation org) {
    final OrganizationDetail orgDetail = new OrganizationDetail();
    orgDetail.setCreationDate(org.getIncorporationDate());
    orgDetail.setCountryCode(org.getIncorporationCountry());
    orgDetail.setCompanyType(org.getBusinessType());
    orgDetail.setIsVcse(org.getIsVcse());
    orgDetail.setStatus(org.getStatus());
    orgDetail.setActive(org.getIsActive());
    return orgDetail;
  }

  /**
   * Converts a {@link ContactDetail} into an {@link Address}
   *
   * @param contactDetail
   * @return an address
   */
  public Address convertContactDetailToAddress(ContactDetail contactDetail) {
    final Address address = new Address();
    address.setStreetAddress(contactDetail.getStreetAddress());
    address.setLocality(contactDetail.getLocality());
    address.setRegion(contactDetail.getRegion());
    address.setPostalCode(contactDetail.getPostalCode());
    address.setCountryCode(contactDetail.getCountryCode());
    address.setCountryName(contactDetail.getCountryName());
    return address;
  }

  /**
   * Converts a {@link Organisation} to an {@link OrganizationIdentifier}
   *
   * @param org
   * @return an org identifier
   */
  public OrganizationIdentifier convertOrgToOrgId(final Organisation org) {
    OrganizationIdentifier identifier = new OrganizationIdentifier();
    identifier.setLegalName(org.getLegalName());
    identifier.setUri(org.getUri());
    identifier.setId(org.getEntityId());
    identifier.setScheme(enumFromString(Scheme.class, org.getRegistryCode()));
    return identifier;
  }

  /**
   * Builds an organisation identifier from the registry code (scheme in the API) concatenated with
   * the entity ID. Inserts 'UNKOWN' placeholders if either / both are null
   *
   * @param registryCode
   * @param entityId
   * @return the org ID, e.g. "GB-COH-001"
   */
  public String buildOrgId(String registryCode, String entityId) {
    return (registryCode == null ? "SCHEME-UNKNOWN" : registryCode) + "-"
        + (entityId == null ? "ID-UNKNOWN" : entityId);
  }

}
