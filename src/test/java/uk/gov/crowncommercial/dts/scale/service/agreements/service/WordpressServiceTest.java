package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
class WordpressServiceTest {
    @MockBean
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
}
