package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;

/**
 * Converts a DB organisation status to a {@link SupplierStatus}
 */
@Component
@RequiredArgsConstructor
public class SupplierStatusConverter extends AbstractConverter<String, SupplierStatus> {

  private final ConverterUtils converterUtils;

  @Override
  protected SupplierStatus convert(final String source) {
    return converterUtils.enumFromString(SupplierStatus.class, source);
  }
}
