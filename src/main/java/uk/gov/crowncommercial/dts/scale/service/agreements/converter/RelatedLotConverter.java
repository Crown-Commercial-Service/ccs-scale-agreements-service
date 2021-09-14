package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RelatedAgreementLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRelatedLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Converts LotRelatedLot from database into RelatedAgreementLot.
 *
 */
@RequiredArgsConstructor
@Component
public class RelatedLotConverter extends AbstractConverter<LotRelatedLot, RelatedAgreementLot> {

  private final AgreementService service;

  @Override
  protected RelatedAgreementLot convert(LotRelatedLot source) {
    /*
     * Introduced this explicit call as a workaround to automated bidirectional mapping issues
     * (infinite loop issues). Couldn't get a more elegant solution to work in the time frame.
     */
    Lot lot = service.getLot(source.getRule().getLotId());
    RelatedAgreementLot ral = new RelatedAgreementLot();
    ral.setCaNumber(lot.getAgreement().getNumber());
    ral.setLotNumber(lot.getNumber());
    ral.setRelationship(source.getRelationship());
    return ral;
  }

}
