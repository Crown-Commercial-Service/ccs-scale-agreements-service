package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import com.rollbar.notifier.Rollbar;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
class WordpressServiceTest {
    @MockitoBean
    private Rollbar rollbar;

    @MockitoBean
    private EhcacheConfig cacheConfig;

    @Autowired
    WordpressService service;

    private String CA_NUMBER = "RM1234";
    private String VALID_SUMMARY = "This is a dummy summary for RM1234";
    private String VALID_END_DATE = "This is a dummy end date for RM1234";

    @Test
    public void testValidateWordpressDataToExtractDataFromValidJSON() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("summary", VALID_SUMMARY);

        String result = service.validateWordpressData("summary", jsonObjectFromWordPress, CA_NUMBER);
        assertEquals(VALID_SUMMARY, result);
    }

    @Test
    public void testValidateWordpressDataToExtractDataFromValidJSONWithoutCorrectField() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("end_date", VALID_END_DATE);

        String result = service.validateWordpressData("summary", jsonObjectFromWordPress, CA_NUMBER);
        assertEquals(null, result);
    }

    @Test
    public void testValidateWordpressDataToExtractDataFromValidJSONWithoutContent() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("summary", "");

        String result = service.validateWordpressData("summary", jsonObjectFromWordPress, CA_NUMBER);
        assertEquals(null, result);
    }

    @Test
    public void testValidateWordpressDataToExtractDataFromValidJSONWithNullContent() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("summary", JSONObject.NULL);

        String result = service.validateWordpressData("summary", jsonObjectFromWordPress, CA_NUMBER);
        assertEquals(null, result);
    }

    @Test
    public void testValidateWordpressDataToExtractDataFromEmptyJSON() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();

        String result = service.validateWordpressData("summary", jsonObjectFromWordPress, CA_NUMBER);
        assertEquals(null, result);
    }

    @Test
    void performWordpressRequest_WhenIOExceptionOccurs_CatchesExceptionAndLogsToRollbar() {
        String invalidEndpoint = "http://localhost:59999/invalid-port-causes-ioexception";

        JSONObject result = service.performWordpressRequest(invalidEndpoint);

        assertNull(result, "Expected result to be null when IOException is caught");
        verify(rollbar).error(
                any(IOException.class),
                eq("Exception when connecting to WordPress API endpoint " + invalidEndpoint)
        );
    }

    @Test
    void performWordpressRequest_WhenJSONExceptionOccurs_CatchesExceptionAndLogsToRollbar() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/bad-json-endpoint", exchange -> {
            byte[] response = "{ malformed json content ...".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.getResponseBody().close();
        });
        server.start();

        String endpoint = "http://localhost:8080/bad-json-endpoint";

        try {
            assertDoesNotThrow(() -> {
                JSONObject result = service.performWordpressRequest(endpoint);
                assertNull(result);
            });

            verify(rollbar).error(any(JSONException.class), eq("Exception when connecting to WordPress API endpoint " + endpoint));
        } finally {
            server.stop(0);
        }
    }
}
