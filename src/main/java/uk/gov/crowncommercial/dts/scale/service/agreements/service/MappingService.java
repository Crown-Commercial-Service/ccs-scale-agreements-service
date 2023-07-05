package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.mapping.AgreementSummaryMapper;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

/**
 * Mapping Service - leverages MapStruct to map DB entities to output models
 */
@Service
public class MappingService {
    public AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);

    /**
     * Map a CommercialAgreement to an AgreementSummary
     */
    public AgreementSummary mapCommercialAgreementToAgreementSummary(CommercialAgreement caModel) {
        AgreementSummary model = agreementSummaryMapper.commercialAgreementToAgreementSummary(caModel);

        return model;
    }
}
