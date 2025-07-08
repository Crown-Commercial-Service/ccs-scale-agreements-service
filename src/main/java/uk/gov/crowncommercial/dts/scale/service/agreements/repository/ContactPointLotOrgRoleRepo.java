package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointReason;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface ContactPointLotOrgRoleRepo extends JpaRepository<ContactPointLotOrgRole, Integer> {

    Optional<ContactPointLotOrgRole> findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(int lotOrganisationRoleId, ContactPointReason contactPointReason);

    @Query("SELECT cplor.contactDetail FROM ContactPointLotOrgRole cplor " +
       "JOIN LotOrganisationRole lor ON cplor.lotOrganisationRoleId = lor.id " +
       "JOIN Organisation o ON lor.organisation.id = o.id " +
       "WHERE o.registryCode = :registryCode AND o.entityId = :entityId AND cplor.primary = true")
    List<ContactDetail> findPrimaryContactDetailsByDuns(@Param("registryCode") String registryCode, @Param("entityId") String entityId);
}

