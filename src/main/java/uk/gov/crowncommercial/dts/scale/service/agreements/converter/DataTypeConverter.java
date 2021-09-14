package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.DataType;

/**
 * Converts String from database into DataType enum.
 *
 */
@Component
public class DataTypeConverter extends AbstractConverter<String, DataType> {

  @Override
  protected DataType convert(String type) {
    if ("integer".equalsIgnoreCase(type)) {
      return DataType.INTEGER;
    } else if ("number".equalsIgnoreCase(type)) {
      return DataType.NUMBER;
    } else if ("string".equalsIgnoreCase(type)) {
      return DataType.STRING;
    } else if ("date".equalsIgnoreCase(type)) {
      return DataType.DATE;
    }
    return null;
  }
}
