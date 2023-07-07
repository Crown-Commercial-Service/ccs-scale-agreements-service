package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.mapping.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

/**
 * Mapping Service - leverages MapStruct to map DB entities to output models
 */
@Service
public class MappingService {
    public AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    public AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);
    public LotDetailMapper lotDetailMapper = Mappers.getMapper(LotDetailMapper.class);
    public DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);
    public AgreementUpdateMapper updateMapper = Mappers.getMapper(AgreementUpdateMapper.class);

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

    /**
     * Map a Lot to a LotDetail
     */
    public LotDetail mapLotToLotDetail(Lot lotModel) {
        LotDetail model = lotDetailMapper.lotToLotDetail(lotModel);

        return model;
    }

    /**
     * Map a CommercialAgreementDocument to a Document
     */
    public Document mapCommercialAgreementDocumentToDocument(CommercialAgreementDocument documentModel) {
        Document model = documentMapper.commercialAgreementDocumentToDocument(documentModel);

        return model;
    }

    /**
     * Map a CommercialAgreementUpdate to an AgreementUpdate
     */
    public AgreementUpdate mapCommercialAgreementUpdateToAgreementUpdate(CommercialAgreementUpdate updateModel) {
        AgreementUpdate model = updateMapper.commercialAgreementUpdateToAgreementUpdate(updateModel);

        return model;
    }
}
