package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Contact point - Lot organisation role (M:M join entity)
 */
@Entity
@Table(name = "contact_point_lot_ors")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactPointLotOrgRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "contact_point_id")
  Integer id;

  @Column(name = "contact_point_name")
  String contactPointName;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "contact_detail_id")
  ContactDetail contactDetail;

  @ManyToOne
  @JoinColumn(name = "contact_point_reason_id")
  ContactPointReason contactPointReason;

  @Column(name = "lot_organisation_role_id")
  int lotOrganisationRoleId;

  @Column(name = "effective_from")
  LocalDate effectiveFrom = LocalDate.now();

  @Column(name = "effective_to")
  LocalDate effectiveTo;

  @Column(name = "primary_ind")
  Boolean primary;

  /**
   * Provide a null-safe default
   *
   * @return true if true, otherwise false
   */
  public Boolean getPrimary() {
    return primary != null ? primary : Boolean.FALSE;
  }
}
