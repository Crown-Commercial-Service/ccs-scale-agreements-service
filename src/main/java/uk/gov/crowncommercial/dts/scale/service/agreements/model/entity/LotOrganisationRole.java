package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Lot organisation role
 */
@Entity
@Table(name = "lot_organisation_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotOrganisationRoles")
public class LotOrganisationRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lot_organisation_role_id")
  Integer id;

  @OneToOne
  @JoinColumn(name = "lot_id")
  Lot lot;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "organisation")
  Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "role_type_id")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "roleType")
  RoleType roleType;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_organisation_role_id")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "contactPointLotOrgRoles")
  Set<ContactPointLotOrgRole> contactPointLotOrgRoles;

  @ManyToOne
  @JoinColumn(name = "trading_organisation_id")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "trading_organisation")
  TradingOrganisation tradingOrganisation;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @Column(name="organisation_status")
  char status = 'A';

  @Column(name = "role_type_id", insertable = false, updatable=false)
  Integer roleTypeId;

  @Column(name="created_by")
  String createdBy;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name="updated_by")
  String updatedBy;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  public LotOrganisationRole map(Function<LotOrganisationRole, LotOrganisationRole> function) {
    return function.apply(this);
  }

  @Override
  public String toString() {
    return "";
  }

  public int hashCode() {
    return this.id.hashCode();
  }

}
