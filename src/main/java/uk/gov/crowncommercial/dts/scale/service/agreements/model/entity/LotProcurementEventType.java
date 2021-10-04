package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Procurement Event Type.
 */
@Entity
@Immutable
@Table(name = "lot_procurement_event_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotProcurementEventType {

  @EmbeddedId
  LotProcurementEventTypeKey key;

  @MapsId("procurementEventTypeId")
  @ManyToOne
  @JoinColumn(name = "procurement_event_type_id")
  ProcurementEventType procurementEventType;

  @Column(name = "mandatory_event_ind")
  Boolean isMandatoryEvent;

  @Column(name = "repeatable_event_ind")
  Boolean isRepeatableEvent;
}
