package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
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
}
