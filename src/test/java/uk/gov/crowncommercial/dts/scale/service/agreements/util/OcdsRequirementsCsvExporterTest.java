package uk.gov.crowncommercial.dts.scale.service.agreements.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ProcurementDataTemplate;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class OcdsRequirementsCsvExporterTest {

    private final OcdsRequirementsCsvExporter exporter = new OcdsRequirementsCsvExporter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testExportToCsvWithSampleData() throws Exception {
        // Create a sample ProcurementDataTemplate with the structure from the provided JSON
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
                    "source": "buyer",
                    "relatesTo": "buyer",
                    "description": "For Information Only",
                    "requirementGroups": [
                        {
                            "OCDS": {
                                "id": "Key Dates",
                                "description": "Your timeline",
                                "requirements": [
                                    {
                                        "OCDS": {
                                            "id": "Question 1",
                                            "title": "Publication of stage 1",
                                            "dataType": "date-time"
                                        },
                                        "nonOCDS": {
                                            "order": 1,
                                            "answered": false,
                                            "mandatory": true,
                                            "multiAnswer": false,
                                            "questionType": "DateTime"
                                        }
                                    },
                                    {
                                        "OCDS": {
                                            "id": "Question 2",
                                            "title": "Clarification period closes",
                                            "dataType": "date-time"
                                        },
                                        "nonOCDS": {
                                            "order": 2,
                                            "answered": false,
                                            "mandatory": true,
                                            "multiAnswer": false,
                                            "questionType": "DateTime"
                                        }
                                    }
                                ]
                            },
                            "nonOCDS": {
                                "task": "Add timeline",
                                "order": 1,
                                "prompt": "Your timeline prompt",
                                "mandatory": false
                            }
                        }
                    ]
                },
                {
                    "id": "Criterion 2",
                    "title": "How to bid including evaluation criteria.",
                    "source": "buyer",
                    "relatesTo": "buyer",
                    "description": "For Information Only",
                    "requirementGroups": [
                        {
                            "OCDS": {
                                "id": "Group 1",
                                "description": "About assessment criteria and evaluation weightings",
                                "requirements": [
                                    {
                                        "OCDS": {
                                            "id": "Question 1",
                                            "title": "About assessment criteria ",
                                            "dataType": "string",
                                            "description": "Assessment criteria description"
                                        },
                                        "nonOCDS": {
                                            "order": 1,
                                            "answered": false,
                                            "mandatory": false,
                                            "multiAnswer": false,
                                            "questionType": "ReadMe"
                                        }
                                    }
                                ]
                            },
                            "nonOCDS": {
                                "task": "Add context",
                                "order": 1,
                                "prompt": "Context prompt",
                                "mandatory": false
                            }
                        }
                    ]
                }
            ]
            """;

        // Parse the JSON and set it as the criteria object
        Object criteria = objectMapper.readValue(criteriaJson, Object.class);
        template.setCriteria(criteria);

        Collection<ProcurementDataTemplate> templates = Arrays.asList(template);

        // Export to CSV
        String csvResult = exporter.exportToCsv(templates);

        // Verify the CSV contains the expected data
        assertNotNull(csvResult);
        assertTrue(csvResult.startsWith("id,templateName,criteria_id,criteria_title,requirement_group_id,requirement_group_description,OCDS_id,OCDS_title,OCDS_data_type"));

        // Check that it contains the expected OCDS requirements
        assertTrue(csvResult.contains("11"));
        assertTrue(csvResult.contains("FC-DOS6-Lot1-Lot2"));
        assertTrue(csvResult.contains("Criterion 1"));
        assertTrue(csvResult.contains("About the procurement competition"));
        assertTrue(csvResult.contains("Key Dates"));
        assertTrue(csvResult.contains("Question 1"));
        assertTrue(csvResult.contains("Publication of stage 1"));
        assertTrue(csvResult.contains("date-time"));
        assertTrue(csvResult.contains("Question 2"));
        assertTrue(csvResult.contains("Clarification period closes"));
        assertTrue(csvResult.contains("Criterion 2"));
        assertTrue(csvResult.contains("How to bid including evaluation criteria."));
        assertTrue(csvResult.contains("Group 1"));
        assertTrue(csvResult.contains("About assessment criteria "));

        // Count the number of lines (header + data rows)
        String[] lines = csvResult.split("\n");
        assertEquals(4, lines.length); // Header + 3 OCDS requirements

        // Verify CSV format is correct
        for (String line : lines) {
            if (!line.isEmpty()) {
                // Each line should have 9 comma-separated values
                String[] values = line.split(",");
                assertEquals(9, values.length);
            }
        }
    }

    @Test
    void testExportToCsvWithEmptyData() {
        Collection<ProcurementDataTemplate> templates = Arrays.asList();
        String csvResult = exporter.exportToCsv(templates);
        
        assertEquals("id,templateName,criteria_id,criteria_title,requirement_group_id,requirement_group_description,OCDS_id,OCDS_title,OCDS_data_type\n", csvResult);
    }

    @Test
    void testExportToCsvWithNullCriteria() {
        ProcurementDataTemplate template = new ProcurementDataTemplate();
        template.setId(1);
        template.setTemplateName("Test Template");
        template.setCriteria(null);

        Collection<ProcurementDataTemplate> templates = Arrays.asList(template);
        String csvResult = exporter.exportToCsv(templates);
        
        assertEquals("id,templateName,criteria_id,criteria_title,requirement_group_id,requirement_group_description,OCDS_id,OCDS_title,OCDS_data_type\n", csvResult);
    }

    @Test
    void testExportToCsvWithNoOcdsRequirements() throws Exception {
        ProcurementDataTemplate template = new ProcurementDataTemplate();
        template.setId(1);
        template.setTemplateName("Test Template");

        // Criteria with no OCDS requirements
        String criteriaJson = """
            [
                {
                    "id": "Criterion 1",
                    "title": "Test Criterion",
                    "requirementGroups": [
                        {
                            "nonOCDS": {
                                "task": "Test task",
                                "requirements": []
                            }
                        }
                    ]
                }
            ]
            """;

        Object criteria = objectMapper.readValue(criteriaJson, Object.class);
        template.setCriteria(criteria);

        Collection<ProcurementDataTemplate> templates = Arrays.asList(template);
        String csvResult = exporter.exportToCsv(templates);
        
        assertEquals("id,templateName,criteria_id,criteria_title,requirement_group_id,requirement_group_description,OCDS_id,OCDS_title,OCDS_data_type\n", csvResult);
    }
}
