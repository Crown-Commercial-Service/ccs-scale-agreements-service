package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;
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
 * Lot Rule.
 */
@Entity
@Immutable
@Table(name = "lot_rules")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotRule implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "lot_rule_id")
  Integer ruleId;

  @Column(name = "lot_id")
  Integer lotId;

  @Column(name = "lot_rule_name")
  String name;

  @Column(name = "evaluation_type")
  String evaluationType;

  @Column(name = "related_application_system_name")
  String service;

  @OneToMany
  @JoinColumn(name = "lot_rule_id")
  private Set<LotRuleTransactionObject> transactionData;

  @OneToMany
  @JoinColumn(name = "lot_rule_id")
  private Set<LotRuleAttribute> lotAttributes;

}
