package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.sql.Timestamp;
import java.time.Instant;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;

/**
 * Converts a DB organisation status to a {@link SupplierStatus}
 */
@Component
public class TimestampConverter extends AbstractConverter<Timestamp, Instant> {

  @Override
  protected Instant convert(final Timestamp source) {
    return source == null ? null : source.toInstant();
  }
}
