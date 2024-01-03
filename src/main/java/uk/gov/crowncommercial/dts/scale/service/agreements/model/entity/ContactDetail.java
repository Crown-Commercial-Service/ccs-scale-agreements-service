package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Table(name = "contact_details")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "contactDetail")
public class ContactDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "contact_detail_id")
  Integer id;

  @Transient
  String name;

  @Column(name = "effective_from")
  LocalDate effectiveFrom = LocalDate.now();

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

  public Boolean isValid(){
    if (streetAddress == null || streetAddress.isEmpty()) {
      if (postalCode == null || postalCode.isEmpty()){
        if (countryCode == null || countryCode.isEmpty()) {
          if (countryName == null || countryName.isEmpty()) {
            return Boolean.FALSE;
          }
        }
      }
    }
    return Boolean.TRUE;
  }
}
