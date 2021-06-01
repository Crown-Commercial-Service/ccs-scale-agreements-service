package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.PartyRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Scheme;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;

/**
 *
 */
class ConverterUtilsTest {

  private final ConverterUtils converterUtils = new ConverterUtils();

  @Test
  void testEnumFromStringPartyRole() {
    assertEquals(PartyRole.FRAMEWORK_OWNER,
        converterUtils.enumFromString(PartyRole.class, "frameworkOwner"));
  }

  @Test
  void testEnumFromStringSupplierStatus() {
    assertEquals(SupplierStatus.ACTIVE,
        converterUtils.enumFromString(SupplierStatus.class, "active"));
  }

  @Test
  void testEnumFromStringScheme() {
    assertEquals(Scheme.GB_SRS, converterUtils.enumFromString(Scheme.class, "GB-SRS"));
  }

}
