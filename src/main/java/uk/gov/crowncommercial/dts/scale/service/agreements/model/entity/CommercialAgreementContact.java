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
@Table(name = "commercial_agreement_contacts")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommercialAgreementContact {

  @Id
  @Column(name = "commercial_agreement_contact_id")
  Integer id;

  @Column(name = "commercial_agreement_id")
  Integer commercialAgreementId;

  @Column(name = "contact_type")
  String type;

  @Column(name = "email_address")
  String email;

}
