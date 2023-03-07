package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.rollbar.notifier.Rollbar;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.MethodNotImplementedException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Agreement Controller.
 *
 */
@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
@Slf4j
public class AgreementController {

    static final String METHOD_NOT_IMPLEMENTED_MSG = "This method is not yet implemented by the API";

    @Autowired
    private Rollbar rollbar;
  
    @Value("${wordpressURL:https://webdev-cms.crowncommercial.gov.uk/}")
    private String wordpressURL;

    private final AgreementService service;
    private final AgreementConverter converter;

    @GetMapping
    public Collection<AgreementSummary> getAgreements() {
        log.debug("getAgreements");
        final List<CommercialAgreement> agreements = service.getAgreements();
        return agreements.stream().map(converter::convertAgreementToSummaryDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{agreement-id}")
    public AgreementDetail getAgreementDetail(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreement: {}", agreementNumber);
        final CommercialAgreement ca = getAgreement(agreementNumber);

        JSONObject jsonData = connectWordpress(wordpressURL + "wp-json/ccs/v1/frameworks/" + agreementNumber);

        if(jsonData != null) {
            String caDescriptionFromWordpress = validateAndLog("summary", jsonData, agreementNumber);
            String caEndDateFromWordpress = validateAndLog("end_date", jsonData, agreementNumber);

            if(caDescriptionFromWordpress != null) ca.setDescription(caDescriptionFromWordpress);
            if(caEndDateFromWordpress != null) ca.setEndDate(LocalDate.parse(caEndDateFromWordpress));
        }

        return converter.convertAgreementToDTO(ca);
    }

    @GetMapping("/{agreement-id}/lots")
    public Collection<LotDetail> getAgreementLots(@PathVariable(value = "agreement-id") final String agreementNumber, @RequestParam Optional<BuyingMethod> buyingMethod) {
        log.debug("getAgreementLots: agreementNumber={}, buyingMethod={}", agreementNumber, buyingMethod);
        final CommercialAgreement ca = getAgreement(agreementNumber);
        Collection<LotDetail> lots = converter.convertLotsToDTOs(ca.getLots());
        if (buyingMethod.isPresent()) {
            return filterLotsByBuyingMethod(lots, buyingMethod.get());
        } else {
            return lots;
        }
    }

    @GetMapping("/{ca-number}/lots/{lot-number}")
    public LotDetail getLot(@PathVariable(value = "ca-number") String caNumber, @PathVariable(value = "lot-number") String lotNumber) {
        log.debug("getLot: caNumber={},lotNumber={}", caNumber, lotNumber);
        Lot lot = service.findLotByAgreementNumberAndLotNumber(caNumber, lotNumber);
        if (lot == null) {
            throw new AgreementNotFoundException(String.format("Lot number '%s' for agreement number '%s' not found", lotNumber, caNumber));
        }

        JSONObject jsonData = connectWordpress(wordpressURL + "wp-json/ccs/v1/frameworks/" + caNumber + "/lot/" + lotNumber.substring(lotNumber.indexOf(" ") + 1));

        if(jsonData != null) {
            String lotDescriptionFromWordpress = validateAndLog("description", jsonData, caNumber);

            lot.setDescription(lotDescriptionFromWordpress != null ? lotDescriptionFromWordpress : " ");
        }

        return converter.convertLotToDTO(lot);
    }

    @GetMapping("/{agreement-id}/documents")
    public Collection<Document> getAgreementDocuments(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreementDocuments: {}", agreementNumber);
        final CommercialAgreement ca = getAgreement(agreementNumber);
        return converter.convertAgreementDocumentsToDTOs(ca.getDocuments());
    }

    @GetMapping("/{agreement-id}/updates")
    public Collection<AgreementUpdate> getAgreementUpdates(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreementUpdates: {}", agreementNumber);
        final CommercialAgreement ca = getAgreement(agreementNumber);
        return converter.convertAgreementUpdatesToDTOs(ca.getUpdates());
    }

    @GetMapping("/agreements/{ca-number}/lots/{lot-number}/suppliers")
    public Collection<Organisation> getLotSuppliers(@PathVariable(value = "ca-number") String caNumber, @PathVariable(value = "lot-number") String lotNumber) {
        log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);
        throw new MethodNotImplementedException(METHOD_NOT_IMPLEMENTED_MSG);
    }

    private CommercialAgreement getAgreement(final String agreementNumber) {
        final CommercialAgreement ca = service.findAgreementByNumber(agreementNumber);
        if (ca == null) {
          throw new AgreementNotFoundException(agreementNumber);
        }
        return ca;
    }

    private Collection<LotDetail> filterLotsByBuyingMethod(Collection<LotDetail> lots, BuyingMethod buyingMethod) {
        return lots.stream()
            .filter(ld -> ld.getRoutesToMarket().stream()
            .filter(rtm -> buyingMethod == rtm.getBuyingMethod()).count() > 0)
            .collect(Collectors.toSet());
    }

    JSONObject connectWordpress(String endpoint) {
	    JSONObject jsonObjectFromWordpress = null;
	  
	    try {
		    URL url = new URL(endpoint);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
        	    rollbar.error(String.format("Wordpress endpoint (%s) returned %s, using agreement service database" ,url.toString(),connection.getResponseCode()));
            } else {
        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        	    JSONTokener tokener = new JSONTokener(bufferedReader);
        	    jsonObjectFromWordpress = new JSONObject(tokener);
          
        	    connection.disconnect();
            }
	    } catch (MalformedURLException e) {
	    	rollbar.error("MalformedURLException when connecting to WordPress API");
	    	log.error("MalformedURLException when connecting to WordPress API: " + e.getMessage());
	    } catch (IOException e) {
	    	rollbar.error("IOException when connecting to WordPress API");
	    	log.error("IOException when connecting to WordPress API: " + e.getMessage());
	    }

	    return jsonObjectFromWordpress;
    }
  
    String validateAndLog(String field, JSONObject jsonData, String caNumber) {
        if (jsonData.has(field) && !jsonData.isNull(field) && jsonData.getString(field).length() != 0) {
            log.info(String.format("Using Wordpress API %s for %s" ,field, caNumber));
            return jsonData.getString(field);
        }
	    return null;
    }
}
