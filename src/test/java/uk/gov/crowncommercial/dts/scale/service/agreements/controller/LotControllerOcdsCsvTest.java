package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.SupplierLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ProcurementDataTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.util.OcdsRequirementsCsvExporter;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LotController.class)
@ContextConfiguration(classes = {LotController.class, OcdsRequirementsCsvExporter.class})
class LotControllerOcdsCsvTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessLogicClient businessLogicClient;

    @MockBean
    private SupplierLogicClient supplierLogicClient;

    @Autowired
    private OcdsRequirementsCsvExporter ocdsRequirementsCsvExporter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetOcdsRequirementsAsCsv() throws Exception {
        // Create sample data
        ProcurementDataTemplate template = new ProcurementDataTemplate();
        template.setId(11);
        template.setTemplateName("FC-DOS6-Lot1-Lot2");
        template.setMandatory(false);

        // Sample criteria JSON structure
        String criteriaJson = """
            [
                {
                    "id": "Criterion 1",
                    "title": "About the procurement competition",
                    "requirementGroups": [
                        {
                            "OCDS": {
                                "id": "Key Dates",
                                "requirements": [
                                    {
                                        "OCDS": {
                                            "id": "Question 1",
                                            "title": "Publication of stage 1",
                                            "dataType": "date-time"
                                        }
                                    }
                                ]
                            }
                        }
                    ]
                }
            ]
            """;

        Object criteria = objectMapper.readValue(criteriaJson, Object.class);
        template.setCriteria(criteria);

        Collection<ProcurementDataTemplate> templates = Arrays.asList(template);

        // Mock the business logic client
        when(businessLogicClient.getEventDataTemplates("RM1043.8", "1", "FC"))
                .thenReturn(templates);

        // Test the endpoint
        mockMvc.perform(get("/agreements/RM1043.8/lots/1/event-types/FC/data-templates/ocds-requirements/csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/csv")))
                .andExpect(header().string("Content-Disposition", 
                        "attachment; filename=\"ocds-requirements-RM1043.8-1-FC.csv\""))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("id,templateName,criteria_id,criteria_title,requirement_group_id,requirement_group_description,OCDS_id,OCDS_title,OCDS_data_type")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("11")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("FC-DOS6-Lot1-Lot2")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Criterion 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("About the procurement competition")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Key Dates")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Question 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Publication of stage 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("date-time")));
    }

    @Test
    void testGetOcdsRequirementsAsCsvNotFound() throws Exception {
        // Mock empty response
        when(businessLogicClient.getEventDataTemplates("RM1043.8", "1", "FC"))
                .thenReturn(Arrays.asList());

        // Test the endpoint
        mockMvc.perform(get("/agreements/RM1043.8/lots/1/event-types/FC/data-templates/ocds-requirements/csv"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOcdsRequirementsAsCsvWithNullData() throws Exception {
        // Mock null response
        when(businessLogicClient.getEventDataTemplates("RM1043.8", "1", "FC"))
                .thenReturn(null);

        // Test the endpoint
        mockMvc.perform(get("/agreements/RM1043.8/lots/1/event-types/FC/data-templates/ocds-requirements/csv"))
                .andExpect(status().isNotFound());
    }
}

