package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.BenefitNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementBenefitRepo;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommercialAgreementBenefitService {

    private final CommercialAgreementBenefitRepo commercialAgreementBenefitRepo;

    /**
     * Delete all benefits that relates to this agreement
     *
     * @param ca CommercialAgreement
     */
    @Transactional
    public void removeBenefits (CommercialAgreement ca){
        if (!commercialAgreementBenefitRepo.findByAgreement(ca).isPresent()) {
            new BenefitNotFoundException(ca.getNumber());
        }
        commercialAgreementBenefitRepo.deleteByAgreementId(ca.getId());
        ca.getBenefits().clear();
        commercialAgreementBenefitRepo.flush();
    }

}
