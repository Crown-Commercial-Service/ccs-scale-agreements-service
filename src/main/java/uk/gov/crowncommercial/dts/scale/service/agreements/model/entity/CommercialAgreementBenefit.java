package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Commercial Agreement Benefit.
 */
@Entity
@Immutable
@Table(name = "commercial_agreement_benefits")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "benefits")
public class CommercialAgreementBenefit {

  @Id
  @Column(name = "commercial_agreement_benefit_id")
  Integer id;

  @Column(name = "benefit_name")
  String name;

  @Column(name = "benefit_description")
  String description;

  @Column(name = "benefit_url")
  String url;

  @Column(name = "order_seq")
  Integer sequence;

}
