package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

/**
*
*/
@Repository
public interface LotRepo extends JpaRepository<Lot, Integer> {

	@Query("SELECT l FROM Lot l where l.number = :lotNumber and l.agreement.number = :caNumber")
	Lot findLotByNumber(@Param("caNumber") String caNumber, @Param("lotNumber") String lotNumber);
}
