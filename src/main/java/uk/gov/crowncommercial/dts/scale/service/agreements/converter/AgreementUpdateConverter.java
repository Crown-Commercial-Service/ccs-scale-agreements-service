package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;

/**
 * Converts CommercialAgreementUpdate from database into AgreementUpdate.
 *
 */
@RequiredArgsConstructor
@Component
public class AgreementUpdateConverter
    extends AbstractConverter<CommercialAgreementUpdate, AgreementUpdate> {

  @Override
  protected AgreementUpdate convert(CommercialAgreementUpdate source) {
    AgreementUpdate update = new AgreementUpdate();
    update.setDate(source.getPublishedDate() != null
        ? source.getPublishedDate().toLocalDateTime().toLocalDate()
        : null);
    update.setText(source.getDescription());
    update.setLinkUrl(source.getUrl());
    return update;
  }

}
