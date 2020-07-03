package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Immutable
@Table(name = "lot_related_lots")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
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
