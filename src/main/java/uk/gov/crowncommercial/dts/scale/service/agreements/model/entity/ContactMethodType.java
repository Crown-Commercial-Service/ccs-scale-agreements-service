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
 * Contact method types (email, phone etc)
 */
@Entity
@Immutable
@Table(name = "contact_method_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactMethodType {

  @Id
  @Column(name = "contact_method_type_id")
  Integer id;

  @Column(name = "contact_method_type_name")
  String name;

  @Column(name = "contact_method_type_description")
  String description;

}
