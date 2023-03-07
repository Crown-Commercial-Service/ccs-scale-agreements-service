package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;

/**
 * Converts CommercialAgreementBenefit from database into String.
 *
 */
@Component
public class AgreementBenefitConverter
    extends AbstractConverter<Collection<CommercialAgreementBenefit>, List<String>> {

  @Override
  protected List<String> convert(Collection<CommercialAgreementBenefit> source) {

    List<String> benefits = new ArrayList<>();

    if (source != null) {
      benefits =
          source.stream().sorted(Comparator.comparingInt(CommercialAgreementBenefit::getSequence))
              .map(CommercialAgreementBenefit::getDescription).collect(Collectors.toList());
    }
    return benefits;
  }
}
