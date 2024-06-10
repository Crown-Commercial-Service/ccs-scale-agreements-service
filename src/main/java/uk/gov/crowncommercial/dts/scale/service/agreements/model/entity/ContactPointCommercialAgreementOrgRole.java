package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Contact point - CA organisation role (M:M join entity)
 */
@Entity
@Immutable
@Table(name = "contact_point_commercial_agreement_ors")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactPointCommercialAgreementOrgRole {

  @Id
  @Column(name = "contact_point_id")
  Integer id;

  @Column(name = "contact_point_name")
  String contactPointName;

  @ManyToOne
  @JoinColumn(name = "contact_detail_id")
  ContactDetail contactDetail;

  @ManyToOne
  @JoinColumn(name = "contact_point_reason_id")
  ContactPointReason contactPointReason;

  @Column(name = "effective_from")
  LocalDate effectiveFrom;

  @Column(name = "effective_to")
  LocalDate effectiveTo;

  @Column(name = "primary_ind")
  Boolean primary;

}
