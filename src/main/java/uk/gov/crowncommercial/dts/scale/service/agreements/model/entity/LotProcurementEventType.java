package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot Procurement Event Type.
 */
@Entity
@Table(name = "lot_procurement_event_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotProcurementEventType {

  @EmbeddedId
  LotProcurementEventTypeKey key;

  @MapsId("lotId")
  @ManyToOne
  @JoinColumn(name = "lot_id")
  Lot lot;

  @MapsId("procurementEventTypeId")
  @ManyToOne
  @JoinColumn(name = "procurement_event_type_id")
  ProcurementEventType procurementEventType;

  @Column(name = "mandatory_event_ind")
  Boolean isMandatoryEvent;

  @Column(name = "repeatable_event_ind")
  Boolean isRepeatableEvent;

  @Column(name = "assessment_tool_id")
  String assessmentToolId;

  @Column(name = "max_repeats")
  Integer maxRepeats;
}
