package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Procurement Event Type.
 */
@Entity
@Immutable
@Table(name = "procurement_event_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcurementEventType {

  @Id
  @Column(name = "procurement_event_type_id")
  Integer id;

  @Column(name = "procurement_event_type_name")
  String name;

  @Column(name = "procurement_event_type_description")
  String description;

  @Column(name = "premarket_activity_ind")
  Boolean preMarketActivity;

}
