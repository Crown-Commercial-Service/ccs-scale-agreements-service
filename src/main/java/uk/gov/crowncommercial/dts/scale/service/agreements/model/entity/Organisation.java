package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * Organisation
 */
@Entity
@Table(name = "organisations")
@Data
@EqualsAndHashCode(exclude = "people")
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "organisation")
public class Organisation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "organisation_id")
  Integer id;

  @Column(name = "entity_id")
  String entityId;

  @Column(name = "registry_code")
  String registryCode;

  @Column(name = "legal_name")
  String legalName;

  @Column(name = "business_type")
  String businessType;

  @Column(name = "organisation_uri")
  String uri;

  @Column(name = "status")
  String status;

  @Column(name = "incorporation_date")
  LocalDate incorporationDate;

  @Column(name = "country_of_incorporation")
  String incorporationCountry;

  @Column(name = "country_name")
  String countryName;

  @Column(name = "is_sme")
  Boolean isSme;

  @Column(name = "is_vcse")
  Boolean isVcse;

  @Column(name = "active")
  Boolean isActive;

  @OneToMany(mappedBy = "organisation")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "people")
  Set<Person> people;

  public Boolean getIsSme() {
    return isSme != null ? isSme : Boolean.FALSE;
  }

  public Boolean getIsVcse() {
    return isVcse != null ? isVcse : Boolean.FALSE;
  }

  public Boolean getIsActive() {
    return isActive != null ? isActive : Boolean.FALSE;
  }

  public Organisation map(Function<Organisation, Organisation> function) {
    return function.apply(this);
  }
}
