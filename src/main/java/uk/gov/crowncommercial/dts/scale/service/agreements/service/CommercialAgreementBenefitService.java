package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.BenefitNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementBenefitRepo;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommercialAgreementBenefitService {

    private final CommercialAgreementBenefitRepo commercialAgreementBenefitRepo;

    public void createBenefits (CommercialAgreementBenefit benefit){
        commercialAgreementBenefitRepo.saveAndFlush(benefit);
    }

    public void removeBenefits (CommercialAgreement ca){
        commercialAgreementBenefitRepo.findByAgreement(ca).orElseThrow(()-> new BenefitNotFoundException(ca.getNumber())).forEach((benefit) -> {
            commercialAgreementBenefitRepo.delete(benefit);
        });
    }

}
