package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;

import java.util.Optional;

public interface LotOrganisationRoleRepo extends JpaRepository<LotOrganisationRole, Integer> {

    LotOrganisationRole findById(int id);

    Optional<LotOrganisationRole> findFirstByLotIdAndOrganisationIdAndRoleTypeId(int lotId, int OrganisationId, int roleTypeId);
}
