package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import com.rollbar.notifier.Rollbar;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@Slf4j
public class WordpressService {
    @Autowired
    private Rollbar rollbar;

    @Value("${wordpressURL:https://webdev-cms.crowncommercial.gov.uk/}")
    private String wordpressBaseURL;

    /**
     * Performs a request against a specified Wordpress endpoint
     */
    public JSONObject performWordpressRequest(String endpoint) {
        JSONObject jsonObjectFromWordpress = null;

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                // Note as an informational item that this endpoint doesn't give us a satisfactory response
                rollbar.info(String.format("Wordpress endpoint (%s) returned %s, using agreement service database", url, connection.getResponseCode()));
            } else {
                // Data has been returned successfully, so use it
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                JSONTokener tokener = new JSONTokener(bufferedReader);
                jsonObjectFromWordpress = new JSONObject(tokener);

                connection.disconnect();
            }
        } catch (MalformedURLException ex) {
            rollbar.error(ex, "MalformedURLException when connecting to WordPress API");
        } catch (IOException ex) {
            rollbar.error(ex, "IOException when connecting to WordPress API");
        }

        return jsonObjectFromWordpress;
    }

    /**
     * Validate that we have specified necessary data from a Wordpress request to use
     */
    public String validateWordpressData(String field, JSONObject jsonData, String caNumber) {
        if (jsonData.has(field) && !jsonData.isNull(field) && jsonData.getString(field).length() != 0) {
            // We look to have the data requested, so use it
            return jsonData.getString(field);
        }

        // We did not have the information that was requested - we should log this to investigate if necessary
        rollbar.info(String.format("Wordpress API did not give us the data item %s for %s", field, caNumber));
        return null;
    }

    /**
     * Expands a supplied CommercialAgreement model with Wordpress data
     */
    public CommercialAgreement getExpandedCommercialAgreement(CommercialAgreement model, String agreementId) {
        // Start by requesting the data we need from the Wordpress API
        JSONObject jsonData = performWordpressRequest(wordpressBaseURL + "wp-json/ccs/v1/frameworks/" + agreementId);

        if(jsonData != null) {
            // We have the data response, so now use it to populate our expansion data
            String agreementDesc = validateWordpressData("summary", jsonData, agreementId);
            String agreementEndDate = validateWordpressData("end_date", jsonData, agreementId);

            if(agreementDesc != null) model.setDescription(agreementDesc);
            if(agreementEndDate != null) model.setEndDate(LocalDate.parse(agreementEndDate));
        }

        return model;
    }
}
