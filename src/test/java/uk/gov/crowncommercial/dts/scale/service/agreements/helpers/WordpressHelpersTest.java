package uk.gov.crowncommercial.dts.scale.service.agreements.helpers;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WordpressHelpersTest {
    private WordpressHelpers wordpressHelpers;

    @BeforeAll
    public void setup() {
        wordpressHelpers = new WordpressHelpers();
    }

    @Test
    public void testValidateAndLogToExtractDataFromValidJSON() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("summary", "This is a dummy summary for RM1234");

        String result = wordpressHelpers.validateAndLog("summary", jsonObjectFromWordPress, "RM1234");
        assertEquals("This is a dummy summary for RM1234", result);
    }

    @Test
    public void testValidateAndLogToExtractDataFromValidJSONWithoutCorrectField() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("end_date", "This is a dummy end date for RM1234");

        String result = wordpressHelpers.validateAndLog("summary", jsonObjectFromWordPress, "RM1234");
        assertEquals(null, result);
    }

    @Test
    public void testValidateAndLogToExtractDataFromValidJSONWithoutContent() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("summary", "");

        String result = wordpressHelpers.validateAndLog("summary", jsonObjectFromWordPress, "RM1234");
        assertEquals(null, result);
    }

    @Test
    public void testValidateAndLogToExtractDataFromValidJSONWithNullContent() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();
        jsonObjectFromWordPress.put("summary", JSONObject.NULL);

        String result = wordpressHelpers.validateAndLog("summary", jsonObjectFromWordPress, "RM1234");
        assertEquals(null, result);
    }

    @Test
    public void testValidateAndLogToExtractDataFromEmptyJSON() throws Exception {
        JSONObject jsonObjectFromWordPress = new JSONObject();

        String result = wordpressHelpers.validateAndLog("summary", jsonObjectFromWordPress, "RM1234");
        assertEquals(null, result);
    }
}
