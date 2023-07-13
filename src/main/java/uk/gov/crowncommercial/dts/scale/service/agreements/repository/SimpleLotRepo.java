package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.SimpleLot;

/**
 * SimpleLot data repository
 */
@Repository
public interface SimpleLotRepo extends JpaRepository<SimpleLot, Integer> {
    SimpleLot findByAgreementNumberAndNumber(String caNumber, String lotNumber);
}
