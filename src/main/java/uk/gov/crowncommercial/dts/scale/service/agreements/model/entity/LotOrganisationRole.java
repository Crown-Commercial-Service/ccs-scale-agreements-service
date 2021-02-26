package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot organisation role
 */
@Entity
@Immutable
@Table(name = "lot_organisation_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotOrganisationRole {

  @Id
  @Column(name = "lot_organisation_role_id")
  Integer id;

  // Lot lot;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "role_type_id")
  RoleType roleType;

  @OneToMany
  @JoinColumn(name = "lot_organisation_role_id")
  Set<ContactPointLotOrgRole> contactPointLotOrgRole;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;
}
