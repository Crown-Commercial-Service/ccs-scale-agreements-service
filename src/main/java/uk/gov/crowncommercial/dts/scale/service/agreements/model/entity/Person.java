package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Person
 */
@Entity
@Immutable
@Table(name = "people")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "people")
public class Person {

  @Id
  @Column(name = "person_id")
  Integer id;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  Organisation organisation;

  @Column(name = "first_name")
  String firstName;

  @Column(name = "last_name")
  String lastName;

  @Column(name = "title")
  String title;

}
