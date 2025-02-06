package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Procurement Question Template.
 */
@Entity
@Table(name = "procurement_question_templates")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ProcurementQuestionTemplate {

  @Id
  @Column(name = "template_id")
  Integer id;

  @Column(name = "template_name")
  String templateName;

  @Column (name= "template_description")
  String description;

  @Column (name ="template_parent")
  Integer parent;

  @Column (name = "template_mandatory")
  Boolean mandatory;

  @Column(name = "created_by")
  String createdBy;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @Column(name = "template_url")
  String templateUrl;

  @Type(type = "jsonb")
  @Column(name = "template_payload")
  Object templatePayload;

//  @Embedded
//  Timestamps timestamps;
}
