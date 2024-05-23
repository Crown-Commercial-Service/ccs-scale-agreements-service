package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
    private final AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    private final AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);
    private final LotDetailMapper lotDetailMapper = Mappers.getMapper(LotDetailMapper.class);
    private final DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);
    private final AgreementUpdateMapper updateMapper = Mappers.getMapper(AgreementUpdateMapper.class);
    private final LotSupplierMapper supplierMapper = Mappers.getMapper(LotSupplierMapper.class);
    private final EventTypeMapper eventTypeMapper = Mappers.getMapper(EventTypeMapper.class);
    private final ProcurementDataTemplateMapper dataTemplateMapper = Mappers.getMapper(ProcurementDataTemplateMapper.class);
    private final QuestionTemplateMapper questionTemplateMapper = Mappers.getMapper(QuestionTemplateMapper.class);

    /**
     * Map a CommercialAgreement to an AgreementSummary
     */
    public AgreementSummary mapCommercialAgreementToAgreementSummary(CommercialAgreement caModel) {
        return agreementSummaryMapper.commercialAgreementToAgreementSummary(caModel);
    }

    /**
     * Map a CommercialAgreement to an AgreementDetail
     */
    public AgreementDetail mapCommercialAgreementToAgreementDetail(CommercialAgreement caModel) {
        return agreementDetailMapper.commercialAgreementToAgreementDetail(caModel);
    }

    /**
     * Map a AgreementDetail to an CommercialAgreement
     */
    public CommercialAgreement mapAgreementDetailToCommercialAgreement(AgreementDetail caModel) {
        return agreementDetailMapper.agreementDetailToCommercialAgreement(caModel);
    }

    /**
     * Map a Lot to a LotDetail
     */
    public LotDetail mapLotToLotDetail(Lot lotModel) {
        return lotDetailMapper.lotToLotDetail(lotModel);
    }

    /**
     * Map a LotDetail to a Lot
     */
    public Lot mapLotDetailToLot(LotDetail lotDetailModel) {

        Lot model = lotDetailMapper.lotDetailToLot(lotDetailModel);

        return model;
    }

    /**
     * Map a CommercialAgreementDocument to a Document
     */
    public Document mapCommercialAgreementDocumentToDocument(CommercialAgreementDocument documentModel) {
        return documentMapper.commercialAgreementDocumentToDocument(documentModel);
    }

    /**
     * Map a CommercialAgreementUpdate to an AgreementUpdate
     */
    public AgreementUpdate mapCommercialAgreementUpdateToAgreementUpdate(CommercialAgreementUpdate updateModel) {
        return updateMapper.commercialAgreementUpdateToAgreementUpdate(updateModel);
    }

    /**
     * Map a LotOrganisationRole to an LotSupplier
     */
    public LotSupplier mapLotOrganisationRoleToLotSupplier(LotOrganisationRole orgRoleModel) {
        return supplierMapper.lotOrganisationRoleToLotSupplier(orgRoleModel);
    }

    /**
     * Map a LotProcurementEventType to an EventType
     */
    public EventType mapLotProcurementEventTypeToEventType(LotProcurementEventType lotEventTypeModel, Lot lotModel) {
        return eventTypeMapper.lotProcurementEventTypeToEventType(lotEventTypeModel, lotModel);
    }

    /**
     * Map a ProcurementQuestionTemplate to a ProcurementDataTemplate
     */
    public ProcurementDataTemplate mapProcurementQuestionTemplateToProcurementDataTemplate(ProcurementQuestionTemplate templateModel) {
        return dataTemplateMapper.procurementQuestionTemplateToProcurementDataTemplate(templateModel);
    }

    /**
     * Map a ProcurementQuestionTemplate to a Document
     */
    public Document mapLotProcurementQuestionTemplateToDocument(ProcurementQuestionTemplate templateModel) {
        return documentMapper.procurementQuestionTemplateToDocument(templateModel);
    }

    /**
     * Map a ProcurementQuestionTemplate to a QuestionTemplate
     */
    public QuestionTemplate mapProcurementQuestionTemplateToQuestionTemplate(ProcurementQuestionTemplate templateModel, Integer groupId) {
        return questionTemplateMapper.procurementQuestionTemplateToQuestionTemplate(templateModel, groupId);
    }

    /**
     * Map a LotSupplier to a Organisation
     */
    public Organisation mapLotSupplierToOrganisation(LotSupplier lotSupplier) {

        return supplierMapper.lotSupplierToOrganisation(lotSupplier);
    }

    /**
     * Map a LotSupplier to a ContactDetail
     */
    public ContactDetail mapLotSupplierToContactDetail(LotSupplier lotSupplier) {

        return supplierMapper.lotSupplierToContactDetail(lotSupplier);
    }
}
