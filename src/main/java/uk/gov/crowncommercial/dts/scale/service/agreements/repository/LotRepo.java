package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Lot Data Repository.
 */
@Repository
public interface LotRepo extends JpaRepository<Lot, Integer> {

  Optional<Lot> findByAgreementNumberAndNumber(String caNumber, String lotNumber);

}
