package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RoleType;

import java.util.Optional;

public interface LotOrganisationRoleRepo extends JpaRepository<LotOrganisationRole, Integer> {

    Optional<LotOrganisationRole> findByLotIdAndOrganisationIdAndRoleType(int lotId, int organisationID, RoleType roleType);
}
