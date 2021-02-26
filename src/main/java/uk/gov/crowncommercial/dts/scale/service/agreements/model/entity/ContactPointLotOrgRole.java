package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Contact point - Lot organisation role (M:M join entity)
 */
@Entity
@Immutable
@Table(name = "contact_point_lot_ors")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactPointLotOrgRole {

  @Id
  @Column(name = "contact_point_id")
  Integer id;

  @ManyToOne
  @JoinColumn(name = "contact_detail_id")
  ContactDetail contactDetail;

  @ManyToOne
  @JoinColumn(name = "contact_point_reason_id")
  ContactPointReason contactPointReason;

  @Column(name = "effective_from")
  LocalDate effectiveFrom;

  @Column(name = "effecive_to")
  LocalDate effeciveTo;

  @Column(name = "primary_ind")
  Boolean primary;
}
