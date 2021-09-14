package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;

/**
 * Converts String from database into Sector.
 *
 */
@Component
public class SectorConverter extends AbstractConverter<Sector, String> {

  @Override
  protected String convert(Sector source) {
    return source == null ? null : source.getName();
  }
}
