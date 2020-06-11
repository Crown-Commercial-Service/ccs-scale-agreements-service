package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

/**
*
*/
@Repository
public interface CommercialAgreementRepo extends JpaRepository<CommercialAgreement, Integer> {

	@Query("SELECT ca FROM CommercialAgreement ca where ca.number = :caNumber")
	CommercialAgreement findAgreementByNumber(@Param("caNumber") String caNumber);
}
