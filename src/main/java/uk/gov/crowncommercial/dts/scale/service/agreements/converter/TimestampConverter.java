package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import java.sql.Timestamp;
import java.time.Instant;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

/**
 * Converts a DB Timestamp to an Instant.
 */
@Component
public class TimestampConverter extends AbstractConverter<Timestamp, Instant> {

  @Override
  protected Instant convert(final Timestamp source) {
    return source == null ? null : source.toInstant();
  }
}
