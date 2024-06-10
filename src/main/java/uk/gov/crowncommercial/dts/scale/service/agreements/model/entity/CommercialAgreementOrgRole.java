package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Commercial agreement organisation role
 */
@Entity
@Immutable
@Table(name = "commercial_agreement_organisation_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommercialAgreementOrgRole {

  @Id
  @Column(name = "commercial_agreement_organisation_role_id")
  Integer id;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "role_type_id")
  RoleType roleType;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "commercial_agreement_organisation_role_id")
  Set<ContactPointCommercialAgreementOrgRole> contactPointCommercialAgreementOrgRoles;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

}
