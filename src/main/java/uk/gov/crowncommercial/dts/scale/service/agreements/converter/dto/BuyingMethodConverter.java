package uk.gov.crowncommercial.dts.scale.service.agreements.converter.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.BuyingMethod;

/**
 * Converts a string name e.g. 'DirectAward' into a <code>BuyingMethod</code>
 */
@Component
public class BuyingMethodConverter implements Converter<String, BuyingMethod> {

  @Override
  public BuyingMethod convert(String name) {
    return BuyingMethod.fromName(name);
  }

}
