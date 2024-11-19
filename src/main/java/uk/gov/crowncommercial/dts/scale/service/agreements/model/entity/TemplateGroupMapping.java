package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

/**
 * Procurement Event Type.
 */
@Entity
@Immutable
@Table(name = "template_group_mapping")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemplateGroupMapping {

  @Id
  @Column(name = "template_group_mapping_id")
  Integer id;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "template_group_id")
  TemplateGroup templateGroup;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "template_id")
  ProcurementQuestionTemplate template;

  @ToString.Exclude
  @Embedded
  private Timestamps timestamps;
}
