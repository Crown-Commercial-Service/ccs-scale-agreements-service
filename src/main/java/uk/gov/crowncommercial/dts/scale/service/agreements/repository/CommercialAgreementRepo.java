package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

import java.util.Optional;

/**
 * Commercial Agreement Data Repository.
 */
@Repository
public interface CommercialAgreementRepo extends JpaRepository<CommercialAgreement, Integer> {

  Optional<CommercialAgreement> findByNumber(String caNumber);
}
