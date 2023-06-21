package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Immutable
@Table(name = "lot_related_lots")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRelatedAgreementLots")
public class LotRelatedLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "lot_id")
  Integer id;

  @Id
  @Column(name = "relationship_description")
  String relationship;

  @Id
  @ManyToOne
  @JoinColumn(name = "lot_rule_id")
  LotRule rule;
}
