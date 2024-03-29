package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

/**
 * OCDS Organization (see
 * https://raw.githubusercontent.com/Crown-Commercial-Service/ccs-scale-api-definitions/master/common/OCDS_Schema.yaml#/components/schemas/Organization)
 */
@Data
public class Organization implements Serializable {

  /**
   * Conclave Organization Id.
   */
  private String id;

  /**
   * Company registered name.
   */
  private String name;

  /**
   * Primary identifier
   */
  private OrganizationIdentifier identifier;

  /**
   * Additional identifiers
   */
  private Set<OrganizationIdentifier> additionalIdentifiers;

  /**
   * Address
   */
  private Address address;

  /**
   * Contact details that can be used for this party.
   */
  private ContactPoint contactPoint;

  /**
   * The party's role(s) in the contracting process
   */
  private Set<PartyRole> roles;

  /**
   * Additional classification information
   */
  private OrganizationDetail details;
}
