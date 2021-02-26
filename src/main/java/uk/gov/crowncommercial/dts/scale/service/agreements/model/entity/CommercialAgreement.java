package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Commercial Agreement.
 */
@Entity
@Immutable
@Table(name = "commercial_agreements")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommercialAgreement {

  @Id
  @Column(name = "commercial_agreement_id")
  Integer id;

  @Column(name = "commercial_agreement_number")
  String number;

  @Column(name = "commercial_agreement_name")
  String name;

  @Column(name = "commercial_agreement_owner")
  String owner;

  @Column(name = "commercial_agreement_description")
  String description;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @Column(name = "agreement_url")
  String detailUrl;

  @OneToMany(mappedBy = "agreement")
  Set<Lot> lots;

  @OneToMany
  @JoinColumn(name = "commercial_agreement_id")
  Set<CommercialAgreementDocument> documents;

  @OneToMany
  @JoinColumn(name = "commercial_agreement_id")
  Set<CommercialAgreementUpdate> updates;
}
