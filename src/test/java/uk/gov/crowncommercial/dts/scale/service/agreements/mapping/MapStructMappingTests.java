package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class MapStructMappingTests {
    private AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);

    private static final String AGREEMENT_NAME = "Agreement Name";
    private static final String AGREEMENT_NUMBER = "RM1234";

    @Test
    public void testCommercialAgreementMapsToAgreementSummary() {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        AgreementSummary outputModel = agreementSummaryMapper.commercialAgreementToAgreementSummary(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }

    @Test
    public void testAgreementSummaryMapsToCommercialAgreement() {
        AgreementSummary sourceModel = new AgreementSummary();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        CommercialAgreement outputModel = agreementSummaryMapper.agreementSummaryToCommercialAgreement(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }
}
