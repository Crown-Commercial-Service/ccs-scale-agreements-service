package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import javax.persistence.*;
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
  Long id;

  @Id
  @ManyToOne
  @JoinColumn(name = "contact_method_type_id")
  ContactMethodType contactMethodType;

  @Column(name = "effective_from")
  LocalDate effectiveFrom;

  @Column(name = "effective_to")
  LocalDate effectiveTo;

  @Column(name = "virtual_address_value")
  String virtualAddress;

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

  @Column(name = "uprn")
  Integer uprn;

}
