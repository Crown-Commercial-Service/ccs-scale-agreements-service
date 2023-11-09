package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

import java.util.Optional;

/**
 * Organisation Data Repository.
 */
@Repository
public interface OrganisationRepo extends JpaRepository<Organisation, Integer> {

    Organisation findById(int id);

    Organisation findByEntityId(String entityId);

    Optional<Organisation> findByLegalName(String legalName);

}