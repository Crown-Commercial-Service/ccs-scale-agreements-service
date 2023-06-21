package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "contactPointReason")
public class ContactPointReason {

  @Id
  @Column(name = "contact_point_reason_id")
  Integer id;

  @Column(name = "contact_point_reason_name")
  String name;

  @Column(name = "contact_point_reason_description")
  String description;

}
