package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Contact point reason
 */
@Entity
@Immutable
@Table(name = "contact_point_reasons")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactPointReason {

  @Id
  @Column(name = "contact_point_reason_id")
  Integer id;

  @Column(name = "contact_point_reason_name")
  String name;

  @Column(name = "contact_point_reason_description")
  String description;

}
