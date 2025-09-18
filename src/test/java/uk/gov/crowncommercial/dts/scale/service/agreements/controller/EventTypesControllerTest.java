package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rollbar.notifier.Rollbar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotEventTypeUpdate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventTypesController.class)
@Import(GlobalErrorHandler.class)
public class EventTypesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BusinessLogicClient businessLogicClient;

    @MockitoBean
    private Rollbar rollbar;

    @Test
    public void testManageEventTypeConfiguration() throws Exception {
        LotEventTypeUpdate lotEventTypeUpdate = new LotEventTypeUpdate();
        lotEventTypeUpdate.setType("PA");
        lotEventTypeUpdate.setMandatoryEvent(false);
        lotEventTypeUpdate.setRepeatableEvent(true);
        lotEventTypeUpdate.setAssessmentToolId("FCA_TOOL_1");
        lotEventTypeUpdate.setMaxRepeats(1);

        org.mockito.Mockito.doNothing().when(businessLogicClient).manageEventTypeConfig(any(LotEventTypeUpdate.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/event-types/manage-type")
                        .content(asJsonString(lotEventTypeUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}