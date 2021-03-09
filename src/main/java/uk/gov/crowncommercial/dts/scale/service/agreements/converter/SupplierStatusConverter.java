package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;

/**
 * Converts a DB organisation status to a {@link SupplierStatus}
 */
@Component
public class SupplierStatusConverter extends AbstractConverter<String, SupplierStatus> {

  @Override
  protected SupplierStatus convert(final String source) {
    return ConverterUtils.enumFromString(SupplierStatus.class, source);
  }
}
