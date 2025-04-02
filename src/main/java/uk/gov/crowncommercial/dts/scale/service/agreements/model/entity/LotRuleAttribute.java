package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot Rule Attribute.
 */
@Entity
@Immutable
@Table(name = "lot_rule_attributes")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotRuleAttribute implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "lot_rule_id")
  Integer ruleId;

  @Id
  @Column(name = "attribute_name")
  String name;

  @Column(name = "attribute_data_type")
  String dataType;

  @Column(name = "value_number")
  BigDecimal valueNumber;

  @Column(name = "value_text")
  String valueText;

}
