package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointReason;

import java.util.Optional;

public interface ContactPointLotOrgRoleRepo extends JpaRepository<ContactPointLotOrgRole, Integer> {

    Optional<ContactPointLotOrgRole> findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(int lotOrganisationRoleId, ContactPointReason contactPointReason);
}

