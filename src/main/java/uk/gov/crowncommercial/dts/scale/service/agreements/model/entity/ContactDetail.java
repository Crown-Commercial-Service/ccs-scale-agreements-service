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
@Table(name = "contact_details")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactDetail {

  @Id
  @Column(name = "contact_detail_id")
  Integer id;

  @Column(name = "effective_from")
  LocalDate effectiveFrom;

  @Column(name = "effective_to")
  LocalDate effectiveTo;

  @Column(name = "street_address")
  String streetAddress;

  @Column(name = "locality")
  String locality;

  @Column(name = "region")
  String region;

  @Column(name = "postal_code")
  String postalCode;

  @Column(name = "country_code")
  String countryCode;

  @Column(name = "country_name")
  String countryName;

  @Column(name = "uprn")
  Integer uprn;

  @Column(name = "emailAddress")
  String emailAddress;

  @Column(name = "telephone_number")
  String telephoneNumber;

  @Column(name = "fax_number")
  String faxNumber;

  @Column(name = "url")
  String url;
}
