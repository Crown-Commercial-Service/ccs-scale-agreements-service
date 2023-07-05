package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.mapping.AgreementDetailMapper;
import uk.gov.crowncommercial.dts.scale.service.agreements.mapping.AgreementSummaryMapper;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

/**
 * Mapping Service - leverages MapStruct to map DB entities to output models
 */
@Service
public class MappingService {
    public AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    public AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);

    /**
     * Map a CommercialAgreement to an AgreementSummary
     */
    public AgreementSummary mapCommercialAgreementToAgreementSummary(CommercialAgreement caModel) {
        AgreementSummary model = agreementSummaryMapper.commercialAgreementToAgreementSummary(caModel);

        return model;
    }

    /**
     * Map a CommercialAgreement to an AgreementDetail
     */
    public AgreementDetail mapCommercialAgreementToAgreementDetail(CommercialAgreement caModel) {
        AgreementDetail model = agreementDetailMapper.commercialAgreementToAgreementDetail(caModel);

        return model;
    }
}
