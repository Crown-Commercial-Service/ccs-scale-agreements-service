package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.BenefitNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementBenefitRepo;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommercialAgreementBenefitService {

    private final CommercialAgreementBenefitRepo commercialAgreementBenefitRepo;


    /**
     * Find all benefits related to the agreement by passing in a CommercialAgreement object.
     *
     * @param ca Commercial Agreement
     * @return Collection<CommercialAgreementBenefit>
     */
    public Collection<CommercialAgreementBenefit> getBenefits (CommercialAgreement ca ){
        return commercialAgreementBenefitRepo.findByAgreement(ca).orElseThrow(()-> new BenefitNotFoundException(ca.getNumber()));
    }

    /**
     * Saves the benefit (write to database)
     *
     * @param benefit CommercialAgreementBenefit
     */
    public void createBenefits (CommercialAgreementBenefit benefit){
        commercialAgreementBenefitRepo.saveAndFlush(benefit);
    }

    /**
     * Delete all benefits that relates to this agreement
     *
     * @param ca CommercialAgreement
     */
    public void removeBenefits (CommercialAgreement ca){
        commercialAgreementBenefitRepo.findByAgreement(ca).orElseThrow(()-> new BenefitNotFoundException(ca.getNumber())).forEach((benefit) -> {
            commercialAgreementBenefitRepo.delete(benefit);
        });
    }

}
