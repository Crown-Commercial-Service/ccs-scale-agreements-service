package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

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

  @PostConstruct
  public void init() {
    modelMapper.addConverter(sectorConverter);
    modelMapper.addConverter(lotTypeConverter);
    modelMapper.addConverter(evaluationTypeConverter);
    modelMapper.addConverter(dataTypeConverter);
    modelMapper.addConverter(relatedLotConverter);
    modelMapper.addConverter(agreementUpdateConverter);
  }

  public AgreementDetail convertAgreementToDTO(CommercialAgreement ca) {
    return modelMapper.map(ca, AgreementDetail.class);
  }

  public AgreementSummary convertAgreementToSummaryDTO(CommercialAgreement ca) {
    return modelMapper.map(ca, AgreementSummary.class);
  }

  public LotDetail convertLotToDTO(Lot lot) {
    return modelMapper.map(lot, LotDetail.class);
  }

  public Collection<AgreementUpdate> convertAgreementUpdatesToDTO(
      Collection<CommercialAgreementUpdate> updates) {
    return updates.stream().map(u -> modelMapper.map(u, AgreementUpdate.class))
        .collect(Collectors.toList());
  }

  public Collection<LotDetail> convertLotsToDTOs(Collection<Lot> lots) {
    return lots.stream().map(l -> modelMapper.map(l, LotDetail.class)).collect(Collectors.toList());
  }

}
