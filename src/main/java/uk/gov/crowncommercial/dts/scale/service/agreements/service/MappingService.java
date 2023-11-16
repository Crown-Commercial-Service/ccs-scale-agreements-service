package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.mapping.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;

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
    public LotSupplierMapper supplierMapper = Mappers.getMapper(LotSupplierMapper.class);
    public EventTypeMapper eventTypeMapper = Mappers.getMapper(EventTypeMapper.class);
    public ProcurementDataTemplateMapper dataTemplateMapper = Mappers.getMapper(ProcurementDataTemplateMapper.class);

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
     * Map a AgreementDetail to an CommercialAgreement
     */
    public CommercialAgreement mapAgreementDetailToCommercialAgreement(AgreementDetail caModel) {
        CommercialAgreement model = agreementDetailMapper.agreementDetailToCommercialAgreement(caModel);

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

    /**
     * Map a LotOrganisationRole to an LotSupplier
     */
    public LotSupplier mapLotOrganisationRoleToLotSupplier(LotOrganisationRole orgRoleModel) {
        LotSupplier model = supplierMapper.lotOrganisationRoleToLotSupplier(orgRoleModel);

        return model;
    }

    /**
     * Map a LotProcurementEventType to an EventType
     */
    public EventType mapLotProcurementEventTypeToEventType(LotProcurementEventType lotEventTypeModel, Lot lotModel) {
        EventType model = eventTypeMapper.lotProcurementEventTypeToEventType(lotEventTypeModel, lotModel);

        return model;
    }

    /**
     * Map a ProcurementQuestionTemplate to a ProcurementDataTemplate
     */
    public ProcurementDataTemplate mapProcurementQuestionTemplateToProcurementDataTemplate(ProcurementQuestionTemplate templateModel) {
        ProcurementDataTemplate model = dataTemplateMapper.procurementQuestionTemplateToProcurementDataTemplate(templateModel);

        return model;
    }

    /**
     * Map a ProcurementQuestionTemplate to a Document
     */
    public Document mapLotProcurementQuestionTemplateToDocument(ProcurementQuestionTemplate templateModel) {
        Document model = documentMapper.procurementQuestionTemplateToDocument(templateModel);

        return model;
    }
}
