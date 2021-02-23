package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

/**
 * Lot Data Repository.
 */
@Repository
public interface LotRepo extends JpaRepository<Lot, Integer> {

  Lot findByAgreementNumberAndNumber(String caNumber, String lotNumber);

  Collection<Lot> findByAgreementNumber(String caNumber);
}
