package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Contact point - Lot person role (M:M join entity)
 */
@Entity
@Immutable
@Table(name = "contact_point_lot_prs")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "contactPointLotOrgRoles")
public class ContactPointLotPersonRole {

  @Id
  @Column(name = "contact_point_id")
  Integer id;

  @Column(name = "contact_point_name")
  String contactPointName;

  @ManyToOne
  @JoinColumn(name = "contact_detail_id")
  ContactDetail contactDetail;

  @ManyToOne
  @JoinColumn(name = "contact_point_reason_id")
  ContactPointReason contactPointReason;

  @Column(name = "effective_from")
  LocalDate effectiveFrom;

  @Column(name = "effective_to")
  LocalDate effectiveTo;

  @Column(name = "primary_ind")
  Boolean primary;

}
