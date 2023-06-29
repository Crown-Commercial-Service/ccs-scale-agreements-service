package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.BuyingMethod;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.WordpressService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    private WordpressService wordpressService;

    @Autowired
    private AgreementConverter agreementConverter;

    /**
     * Returns a list of all Commercial Agreements
     */
    @Cacheable(value = "getAgreements")
    public List<AgreementSummary> getAgreementsList() {
        // Fetch the list of agreements from the service
        final List<CommercialAgreement> agreements = agreementService.getAgreements();

        // Now convert the list to the objects we want to return
        return agreements.stream().map(agreementConverter::convertAgreementToSummaryDTO).collect(Collectors.toList());
    }

    /**
     * Returns a specific AgreementDetail object based on a requested ID
     */
    @Cacheable
    public AgreementDetail getAgreementDetail(String agreementId) {
        AgreementDetail model = new AgreementDetail();

        // Fetch the Commercial Agreement from the service
        CommercialAgreement agreementModel = agreementService.findAgreementByNumber(agreementId);

        if (agreementModel != null) {
            // We now need to enhance the agreement model we've retrieved with data from the Wordpress API, so do that
            agreementModel = wordpressService.getExpandedCommercialAgreement(agreementModel, agreementId);

            // Now we should have a complete agreement model - convert it to the format we desire, and then return it
            model = agreementConverter.convertAgreementToDTO(agreementModel);
        }

        return model;
    }

    /**
     * Returns a list of LotDetail objects which relate to a specified agreement
     */
    @Cacheable
    public Collection<LotDetail> getLotsForAgreement(String agreementId, Optional<BuyingMethod> buyingMethod) {
        Collection<LotDetail> model = null;

        // Fetch the commercial agreement from the service
        CommercialAgreement agreementModel = agreementService.findAgreementByNumber(agreementId);

        if (agreementModel != null) {
            // Now convert the lots specified in the agreement into the format we want to return
            model = agreementConverter.convertLotsToDTOs(agreementModel.getLots());

            // Finally, if we're given a buying method we need to filter our results to just those matching the method specified
            if (buyingMethod.isPresent()) {
                model = model.stream()
                        .filter(lot -> lot.getRoutesToMarket().stream()
                                .filter(route -> buyingMethod.get() == route.getBuyingMethod()).count() > 0)
                        .collect(Collectors.toSet());
            }
        }

        return model;
    }
}
