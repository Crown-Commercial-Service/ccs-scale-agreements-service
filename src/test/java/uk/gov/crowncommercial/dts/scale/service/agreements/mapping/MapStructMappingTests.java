package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Note these tests don't cover all aspects of the mapping - they're intended to ensure that mapping functions at a model level, not to cover every possible mapping option
 */
@SpringBootTest
@ActiveProfiles("test")
public class MapStructMappingTests {
    private AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    private AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);
    private LotDetailMapper lotDetailMapper = Mappers.getMapper(LotDetailMapper.class);

    private static final String AGREEMENT_NAME = "Agreement Name";
    private static final String AGREEMENT_NUMBER = "RM1234";
    private static final String LOT_NUMBER = "Lot 1";
    private static final String LOT_NAME = "Name of Lot";

    @Test
    public void testCommercialAgreementMapsToAgreementSummary() throws Exception {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        AgreementSummary outputModel = agreementSummaryMapper.commercialAgreementToAgreementSummary(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }

    @Test
    public void testCommercialAgreementMapsToAgreementDetail() throws Exception {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        AgreementDetail outputModel = agreementDetailMapper.commercialAgreementToAgreementDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }

    @Test
    public void testLotMapsToLotDetail() throws Exception {
        Lot sourceModel = new Lot();
        sourceModel.setNumber(LOT_NUMBER);
        sourceModel.setName(LOT_NAME);

        LotDetail outputModel = lotDetailMapper.lotToLotDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }
}
