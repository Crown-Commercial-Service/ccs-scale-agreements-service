package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Procurement Question Template.
 */
@Entity
@Immutable
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

  @Column(name = "template_url")
  String templateUrl;

  @Type(type = "jsonb")
  @Column(name = "template_payload")
  Object templatePayload;

//  @Embedded
//  Timestamps timestamps;
}
