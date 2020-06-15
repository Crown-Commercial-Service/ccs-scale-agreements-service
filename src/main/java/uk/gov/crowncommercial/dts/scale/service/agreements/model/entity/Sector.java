package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Immutable
@Table(name = "sectors")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sector {

  @Id
  @Column(name = "sector_code")
  String code;

  @Column(name = "sector_name")
  String name;

  @Column(name = "sector_description")
  String description;

}
