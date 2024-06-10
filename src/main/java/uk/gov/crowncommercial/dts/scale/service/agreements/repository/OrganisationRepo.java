package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

import java.util.Optional;

public interface OrganisationRepo extends JpaRepository<Organisation, Integer> {

    Optional<Organisation> findByLegalName(String legalName);

}
