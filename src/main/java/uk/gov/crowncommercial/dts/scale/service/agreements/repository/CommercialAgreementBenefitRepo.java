package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;

import java.util.Optional;
import java.util.Set;

public interface CommercialAgreementBenefitRepo extends JpaRepository<CommercialAgreementBenefit, Integer> {

    Optional<Set<CommercialAgreementBenefit>> findByAgreement(CommercialAgreement ca);

}
