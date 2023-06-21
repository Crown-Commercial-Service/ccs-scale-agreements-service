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
 * CA / Lot organisational role type
 */
@Entity
@Immutable
@Table(name = "role_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "roleType")
public class RoleType {

  @Id
  @Column(name = "role_type_id")
  Integer id;

  @Column(name = "role_type_name")
  String name;

  @Column(name = "role_domain")
  String roleDomain;

}
