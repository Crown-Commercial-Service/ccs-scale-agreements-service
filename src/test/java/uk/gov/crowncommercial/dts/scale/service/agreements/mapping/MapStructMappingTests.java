package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class MapStructMappingTests {
    private AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    private AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);

    private static final String AGREEMENT_NAME = "Agreement Name";
    private static final String AGREEMENT_NUMBER = "RM1234";

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
        // Note this test covers a very simplified version of the mapping - it's a very complex one
        // The intent here is not to test all aspects of the mapping (as there would be infinite combinations of tests needed) - it's to prove that mapping occurs
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        AgreementDetail outputModel = agreementDetailMapper.commercialAgreementToAgreementDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }
}
