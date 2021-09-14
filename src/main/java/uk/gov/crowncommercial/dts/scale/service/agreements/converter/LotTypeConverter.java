package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;

/**
 * Converts String from database into LotType enum.
 *
 */
@Component
public class LotTypeConverter extends AbstractConverter<String, LotType> {

  @Override
  protected LotType convert(String type) {
    if ("Products".equalsIgnoreCase(type)) {
      return LotType.PRODUCT;
    } else if ("Services".equalsIgnoreCase(type)) {
      return LotType.SERVICE;
    } else if ("Products and Services".equalsIgnoreCase(type)) {
      return LotType.PRODUCT_AND_SERVICE;
    }
    return null;
  }

}
