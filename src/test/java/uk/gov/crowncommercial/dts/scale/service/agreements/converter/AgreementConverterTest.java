package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

@SpringBootTest
@ActiveProfiles("test")
public class AgreementConverterTest {


  @Autowired
  AgreementConverter converter;

  @Test
  public void testAgreementDetail() {

    Lot lot = new Lot();
    lot.setNumber("Lot 1");
    lot.setName("Test Lot");
    Set<Lot> lots = new HashSet<>();
    lots.add(lot);

    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now();

    CommercialAgreement ca = new CommercialAgreement();
    ca.setNumber("R0001");
    ca.setName("Agreement 1");
    ca.setDescription("Agreement description");
    ca.setStartDate(startDate);
    ca.setEndDate(endDate);
    ca.setDetailUrl("https://");
    ca.setLots(lots);

    AgreementDetail agreement = converter.convertAgreementToDTO(ca);

    assertEquals("R0001", agreement.getNumber());
    assertEquals("Agreement 1", agreement.getName());
    assertEquals("Agreement description", agreement.getDescription());
    assertEquals(startDate, agreement.getStartDate());
    assertEquals(endDate, agreement.getEndDate());
    assertEquals("https://", agreement.getDetailUrl());

    LotSummary lotSummary = agreement.getLots().stream().findFirst().get();
    assertEquals("Test Lot", lotSummary.getName());
    assertEquals("Lot 1", lotSummary.getNumber());
  }

  @Test
  public void testLotDetail() {

    Lot lot = new Lot();
    lot.setNumber("Lot 1");
    lot.setName("Test Lot");
    lot.setDescription("Description");
    lot.setLotType("PRODUCT");

    LotDetail lotDetail = converter.convertLotToDTO(lot);

    assertEquals("Test Lot", lotDetail.getName());
    assertEquals("Lot 1", lotDetail.getNumber());
    assertEquals(LotType.PRODUCT, lotDetail.getType());
    assertEquals("Description", lotDetail.getDescription());
  }

}
