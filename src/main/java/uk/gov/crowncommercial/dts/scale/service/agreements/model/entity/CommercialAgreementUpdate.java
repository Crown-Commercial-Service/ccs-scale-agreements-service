package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "updates")
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
