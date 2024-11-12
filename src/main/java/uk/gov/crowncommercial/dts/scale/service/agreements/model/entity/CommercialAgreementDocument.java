package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Commercial Agreement Document.
 */
@Entity
@Immutable
@Table(name = "commercial_agreement_documents")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommercialAgreementDocument {

  @Id
  @Column(name = "commercial_agreement_document_id")
  Integer id;

  @Column(name = "document_name")
  String name;

  @Column(name = "document_description")
  String description;

  @Column(name = "document_url")
  String url;

  @Column(name = "document_type")
  String documentType;

  @Column(name = "document_version")
  Integer version;

  @Column(name = "language")
  String language;

  @Column(name = "format")
  String format;

  @Column(name = "published_date")
  Timestamp publishedDate;

  @Column(name = "modified_at")
  Timestamp modifiedDate;

}
