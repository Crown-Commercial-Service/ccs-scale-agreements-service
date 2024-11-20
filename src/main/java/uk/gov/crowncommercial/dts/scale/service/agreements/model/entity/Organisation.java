package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.*;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidOrganisationException;

/**
 * Organisation
 */
@Entity
@Table(name = "organisations")
@Data
@EqualsAndHashCode(exclude = "people")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Organisation {

  @Id
  @Column(name = "organisation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  public void isValid(){
    if (legalName == null || legalName.isEmpty()) {throw new InvalidOrganisationException("legalName");}
    if (registryCode == null || registryCode.isEmpty()) {throw new InvalidOrganisationException("registryCode");}
    if (entityId == null || entityId.isEmpty()) {throw new InvalidOrganisationException("entityId");}
    if (incorporationDate == null) {throw new InvalidOrganisationException("incorporationDate");}
    if (incorporationCountry == null || incorporationCountry.isEmpty()) {throw new InvalidOrganisationException("incorporationCountry");}
  }

  public void isValidForPartialUpdate(){

    if (legalName != null && !legalName.isEmpty() || entityId != null && !entityId.isEmpty() && registryCode != null && !registryCode.isEmpty()) {
    } else {
      throw new InvalidOrganisationException();
    }
  }
}
