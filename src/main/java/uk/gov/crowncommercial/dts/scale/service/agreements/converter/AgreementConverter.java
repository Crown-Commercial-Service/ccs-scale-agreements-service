package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collection;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;

/**
 * Encapsulates conversion logic between DTOs and Entities.
 */
@RequiredArgsConstructor
@Component
public class AgreementConverter {

  @Autowired
  private ModelMapper modelMapper;

  private final LotTypeConverter lotTypeConverter;
  private final EvaluationTypeConverter evaluationTypeConverter;
  private final SectorConverter sectorConverter;
  private final DataTypeConverter dataTypeConverter;
  private final RelatedLotConverter relatedLotConverter;
  private final AgreementUpdateConverter agreementUpdateConverter;
  private final RouteToMarketConverter routeToMarketConverter;
  private final AgreementContactsConverter agreementContactsConverter;
  private final LotSupplierPropertyMap lotSupplierPropertyMap;
  private final TimestampConverter timestampConverter;
  private final AgreementOwnerConverter agreementOwnerConverter;
  private final AgreementBenefitConverter agreementBenefitConverter;
  private final EventTypeConverter eventTypeConverter;

  @PostConstruct
  public void init() {
    modelMapper.addConverter(sectorConverter);
    modelMapper.addConverter(lotTypeConverter);
    modelMapper.addConverter(evaluationTypeConverter);
    modelMapper.addConverter(dataTypeConverter);
    modelMapper.addConverter(relatedLotConverter);
    modelMapper.addConverter(agreementUpdateConverter);
    modelMapper.addConverter(routeToMarketConverter);
    modelMapper.addConverter(timestampConverter);
    modelMapper.addConverter(eventTypeConverter);

    /*
     * Specific mismatched properties / converters (do not set globally on modelMapper)
     */
    modelMapper.createTypeMap(CommercialAgreement.class, AgreementDetail.class)
        .addMappings(mapper -> mapper.using(agreementContactsConverter)
            .map(CommercialAgreement::getOrganisationRoles, AgreementDetail::setContacts))
        .addMappings(mapper -> mapper.using(agreementOwnerConverter)
            .map(CommercialAgreement::getOrganisationRoles, AgreementDetail::setOwner))
        .addMappings(mapper -> mapper.using(agreementBenefitConverter)
            .map(CommercialAgreement::getBenefits, AgreementDetail::setBenefits));

    modelMapper.createTypeMap(LotOrganisationRole.class, LotSupplier.class)
        .addMappings(lotSupplierPropertyMap);
  }

  public AgreementDetail convertAgreementToDTO(final CommercialAgreement ca) {
    return modelMapper.map(ca, AgreementDetail.class);
  }

  public AgreementSummary convertAgreementToSummaryDTO(final CommercialAgreement ca) {
    return modelMapper.map(ca, AgreementSummary.class);
  }

  public LotDetail convertLotToDTO(final Lot lot) {
    return modelMapper.map(lot, LotDetail.class);
  }

  public Collection<AgreementUpdate> convertAgreementUpdatesToDTOs(
      final Collection<CommercialAgreementUpdate> updates) {
    return updates.stream().map(u -> modelMapper.map(u, AgreementUpdate.class))
        .collect(Collectors.toList());
  }

  public Collection<LotDetail> convertLotsToDTOs(final Collection<Lot> lots) {
    return lots.stream().map(l -> modelMapper.map(l, LotDetail.class)).collect(Collectors.toList());
  }

  public Collection<Document> convertAgreementDocumentsToDTOs(
      final Collection<CommercialAgreementDocument> lots) {
    return lots.stream().map(l -> modelMapper.map(l, Document.class)).collect(Collectors.toList());
  }

  public Collection<LotSupplier> convertLotOrgRolesToLotSupplierDTOs(
      final Collection<LotOrganisationRole> lotOrgRoles) {
    return lotOrgRoles.stream().map(lor -> modelMapper.map(lor, LotSupplier.class))
        .collect(Collectors.toSet());
  }

  public Collection<EventType> convertLotProcurementEventTypesToDTOs(
      final Collection<LotProcurementEventType> lotProcurementEventTypes) {
    return lotProcurementEventTypes.stream().map(lpet -> modelMapper.map(lpet, EventType.class))
        .collect(Collectors.toSet());
  }

  public Collection<ProcurementDataTemplate> convertLotProcurementQuestionTemplateToDataTemplates(
      final Collection<LotProcurementQuestionTemplate> lotProcurementQuestionTemplates,
      final String eventType) {
    return lotProcurementQuestionTemplates.stream()
        .filter(t -> t.getProcurementQuestionTemplate().getTemplatePayload() != null
            && eventType.equalsIgnoreCase(t.getProcurementEventType().getName()))
        .map(t -> getDataTemplate(t.getProcurementQuestionTemplate()))
        .collect(Collectors.toSet());
  }

  public ProcurementDataTemplate getDataTemplate(ProcurementQuestionTemplate questionTemplate){
    ProcurementDataTemplate template = new ProcurementDataTemplate();
    template.setId(questionTemplate.getId());
    template.setTemplateName(questionTemplate.getTemplateName());
    template.setMandatory(questionTemplate.getMandatory());
    template.setParent(questionTemplate.getParent());
    template.setCriteria(questionTemplate.getTemplatePayload());
    return template;
  }

  public Collection<Document> convertLotProcurementQuestionTemplateToDocumentTemplates(
      final Collection<LotProcurementQuestionTemplate> lotProcurementQuestionTemplates,
      final String eventType) {
    return lotProcurementQuestionTemplates.stream()
        .filter(t -> t.getProcurementQuestionTemplate().getTemplateUrl() != null
            && eventType.equalsIgnoreCase(t.getProcurementEventType().getName()))
        .map(t -> {
          Document doc = new Document();
          doc.setUrl(t.getProcurementQuestionTemplate().getTemplateUrl());
          return doc;
        }).collect(Collectors.toSet());
  }
}
