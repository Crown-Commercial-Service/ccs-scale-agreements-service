package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
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

  public void setId(Integer id) {
    this.id = id;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public void setRegistryCode(String registryCode) {
    this.registryCode = registryCode;
  }

  public void setLegalName(String legalName) {
    this.legalName = legalName;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setIncorporationDate(LocalDate incorporationDate) {
    this.incorporationDate = incorporationDate;
  }

  public void setIncorporationCountry(String incorporationCountry) {
    this.incorporationCountry = incorporationCountry;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public void setSme(Boolean sme) {
    isSme = sme;
  }

  public void setVcse(Boolean vcse) {
    isVcse = vcse;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public void setPeople(Set<Person> people) {
    this.people = people;
  }

  public Organisation map(Function<Organisation, Organisation> function) {
    return function.apply(this);
  }

}
