package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
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
@Table(name = "organisations")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Organisation {

  @Id
  @Column(name = "organisation_id")
  Integer id;

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

  @Column(name = "is_sme")
  Boolean isSme;

  @Column(name = "is_vcse")
  Boolean isVcse;

  @Column(name = "active")
  Boolean active;

}
