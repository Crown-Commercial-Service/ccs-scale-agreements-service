package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot Procurement Question Template.
 */
@Entity
@Immutable
@Table(name = "lot_procurement_question_templates")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotProcurementQuestionTemplate {

  @EmbeddedId
  LotProcurementQuestionTemplateKey key;

  @ManyToOne
  @JoinColumn(name = "template_id", insertable = false, updatable = false)
  ProcurementQuestionTemplate procurementQuestionTemplate;

  @ManyToOne
  @JoinColumn(name = "procurement_event_type_id", insertable = false, updatable = false)
  ProcurementEventType procurementEventType;
}
