package uk.gov.crowncommercial.dts.scale.service.agreements.converter.dto;

import static java.util.Optional.ofNullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.BuyingMethod;

/**
 * Converts a string name e.g. 'DirectAward' into a <code>BuyingMethod</code>
 */
@Component
public class BuyingMethodConverter implements Converter<String, BuyingMethod> {

  static final String ERR_MSG_TEMPLATE = "Buying method value invalid. Valid values are: %s";

  @Override
  public BuyingMethod convert(String name) {
    return ofNullable(BuyingMethod.getBuyingMethodFromName(name)).orElseThrow(
        () -> new IllegalArgumentException(String.format(ERR_MSG_TEMPLATE, BuyingMethod.names())));
  }

}
