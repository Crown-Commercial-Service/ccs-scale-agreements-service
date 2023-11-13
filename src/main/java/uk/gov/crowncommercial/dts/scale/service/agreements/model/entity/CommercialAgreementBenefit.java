package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.*;

import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Commercial Agreement Benefit.
 */
@Entity
@Table(name = "commercial_agreement_benefits")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "benefits")
public class CommercialAgreementBenefit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "commercial_agreement_benefit_id")
  Integer id;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "commercial_agreement_id")
  CommercialAgreement agreement;

  @Column(name = "benefit_name")
  String name;

  @Column(name = "benefit_description")
  String description;

  @Column(name = "benefit_url")
  String url;

  @Column(name = "order_seq")
  Integer sequence;

  @Override
  public String toString() {
    return "";
  }

  public int hashCode() {
    return this.name.hashCode();
  }
}
