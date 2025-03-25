package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Procurement Question Template.
 */
@Entity
@Table(name = "procurement_question_templates")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcurementQuestionTemplate {

  @Id
  @Column(name = "template_id")
  Integer id;

  @Column(name = "template_name")
  String templateName;

  @Column(name = "template_description")
  String description;

  @Column(name = "template_parent")
  Integer parent;

  @Column(name = "template_mandatory")
  Boolean mandatory;

  @Column(name = "created_by")
  String createdBy;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @Column(name = "template_url")
  String templateUrl;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "template_payload")
  Object templatePayload;
}