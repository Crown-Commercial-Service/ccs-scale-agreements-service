package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;

import java.util.Optional;
import java.util.Set;

public interface CommercialAgreementBenefitRepo extends JpaRepository<CommercialAgreementBenefit, Integer> {

    Optional<Set<CommercialAgreementBenefit>> findByAgreement(CommercialAgreement ca);

    @Transactional
    @Modifying
    void deleteAllByAgreement(CommercialAgreement ca);

    @Transactional
    @Modifying
    @Query("DELETE FROM CommercialAgreementBenefit e WHERE e.agreement.id = :agreementId")
    void deleteByAgreementId(@Param("agreementId") int agreementId);
}