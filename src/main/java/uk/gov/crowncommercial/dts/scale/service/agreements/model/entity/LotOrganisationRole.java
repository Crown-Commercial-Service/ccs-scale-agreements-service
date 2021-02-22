package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
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
  Long id;

  // Lot lot;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "role_type_id")
  RoleType roleType;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;
}
