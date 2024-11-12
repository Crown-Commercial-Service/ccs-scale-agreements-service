package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot Rule.
 */
@Entity
@Immutable
@Table(name = "lot_rule_transaction_objects")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotRuleTransactionObject implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "lot_rule_id")
  Integer ruleId;

  @Id
  @Column(name = "object_name")
  String name;

  @Id
  @Column(name = "object_reference")
  String location;
}
