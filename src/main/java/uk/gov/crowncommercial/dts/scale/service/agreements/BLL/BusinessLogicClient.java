package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serves as an intermediary level between the Controllers and the Services, where we can retrieve, format and cache data appropriately
 * Everything in here should be cached
 */
@Component
public class BusinessLogicClient {
    @Autowired
    private AgreementService agreementService;

    @Autowired
    private AgreementConverter agreementConverter;

    /**
     * Returns a list of all Commercial Agreements
     */
    @Cacheable(value = "getAgreements")
    public List<AgreementSummary> getAgreementsList() {
        // Fetch the list of agreements from the Agreement Service
        final List<CommercialAgreement> agreements = agreementService.getAgreements();

        // Now convert the list to the objects we want to return
        return agreements.stream().map(agreementConverter::convertAgreementToSummaryDTO).collect(Collectors.toList());
    }
}
