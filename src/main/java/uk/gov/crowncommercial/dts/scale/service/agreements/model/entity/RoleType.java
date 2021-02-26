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
 *
 */
@Entity
@Immutable
@Table(name = "role_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleType {

  @Id
  @Column(name = "role_type_id")
  Integer id;

  @Column(name = "role_type_name")
  String name;

  @Column(name = "role_domain")
  String roleDomain;

}
