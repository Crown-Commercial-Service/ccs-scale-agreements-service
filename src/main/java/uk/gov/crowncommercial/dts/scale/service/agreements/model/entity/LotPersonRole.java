package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot person role
 */
@Entity
@Immutable
@Table(name = "lot_people_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotPersonRole {

  @Id
  @Column(name = "lot_person_role_id")
  Integer id;

  @ManyToOne
  @JoinColumn(name = "person_id")
  Person person;

  @ManyToOne
  @JoinColumn(name = "role_type_id")
  RoleType roleType;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_person_role_id")
  Set<ContactPointLotPersonRole> contactPointLotPersonRole;
}
