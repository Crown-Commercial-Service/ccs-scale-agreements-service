package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;

import java.util.Collection;
import java.util.Optional;

public interface CommercialAgreementBenefitRepo extends JpaRepository<CommercialAgreementBenefit, Integer> {

    Optional<Collection<CommercialAgreementBenefit>> findByAgreement(CommercialAgreement ca);

}
