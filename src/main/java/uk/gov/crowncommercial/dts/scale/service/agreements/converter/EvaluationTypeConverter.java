package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EvaluationType;

/**
 * Converts String from database into EvaluationType enum.
 *
 */
@Component
public class EvaluationTypeConverter extends AbstractConverter<String, EvaluationType> {

  @Override
  protected EvaluationType convert(String type) {
    if ("flag".equalsIgnoreCase(type)) {
      return EvaluationType.FLAG;
    } else if ("complex".equalsIgnoreCase(type)) {
      return EvaluationType.COMPLEX;
    } else if ("equal".equalsIgnoreCase(type)) {
      return EvaluationType.EQUAL;
    } else if ("greater".equalsIgnoreCase(type)) {
      return EvaluationType.GREATER;
    } else if ("less".equalsIgnoreCase(type)) {
      return EvaluationType.LESS;
    }
    return null;
  }
}
