package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

/**
 * Simplified version of Lot - heavy load call is abstracted here to avoid performance hit on the core Lot
 */
@Entity
@Immutable
@EqualsAndHashCode(exclude = "agreement")
@Table(name = "lots")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@javax.persistence.Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lots")
public class SimpleLot {
    @Id
    @Column(name = "lot_id")
    Integer id;

    @Column(name = "lot_number")
    String number;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "commercial_agreement_id")
    CommercialAgreement agreement;

    @ToString.Exclude
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotOrganisationRoles")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    Set<LotOrganisationRole> organisationRoles;

    @ToString.Exclude
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotOrganisationRoles")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", insertable = false, updatable = false)
    @Where(clause="role_type_id = '2' and organisation_status = 'A'")
    Set<LotOrganisationRole> activeOrganisationRoles;
}
