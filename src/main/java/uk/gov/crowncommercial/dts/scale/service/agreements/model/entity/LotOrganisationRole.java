package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;

import lombok.ToString;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot organisation role
 */
@Entity
@Table(name = "lot_organisation_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotOrganisationRole {

  @Id
  @Column(name = "lot_organisation_role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

   @Column(name = "lot_id")
   int lotId;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "role_type_id")
  RoleType roleType;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_organisation_role_id")
  @ToString.Exclude
  Set<ContactPointLotOrgRole> contactPointLotOrgRoles;

  @ManyToOne
  @JoinColumn(name = "trading_organisation_id")
  TradingOrganisation tradingOrganisation;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @Column(name="organisation_status")
  char status;

  @Column(name="created_by")
  String createdBy;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name="updated_by")
  String updatedBy;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}
