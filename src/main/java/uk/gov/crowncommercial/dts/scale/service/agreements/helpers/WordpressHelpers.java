package uk.gov.crowncommercial.dts.scale.service.agreements.helpers;

import com.rollbar.notifier.Rollbar;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// TODO: Remove this class
@Component
@Slf4j
public class WordpressHelpers {
    @Autowired
    private Rollbar rollbar;

    public JSONObject connectWordpress(String endpoint) {
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

    public String validateAndLog(String field, JSONObject jsonData, String caNumber) {
        if (jsonData.has(field) && !jsonData.isNull(field) && jsonData.getString(field).length() != 0) {
            log.info(String.format("Using Wordpress API %s for %s" ,field, caNumber));
            return jsonData.getString(field);
        }
        return null;
    }
}
