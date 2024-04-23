package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Commercial Agreement Update.
 */
@Entity
@Immutable
@Table(name = "commercial_agreement_updates")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommercialAgreementUpdate {

  @Id
  @Column(name = "commercial_agreement_update_id")
  Integer id;

  @Column(name = "update_name")
  String name;

  @Column(name = "update_description")
  String description;

  @Column(name = "update_url")
  String url;

  @Column(name = "published_date")
  Timestamp publishedDate;

}
