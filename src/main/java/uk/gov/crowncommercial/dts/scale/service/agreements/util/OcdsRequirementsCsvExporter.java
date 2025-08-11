package uk.gov.crowncommercial.dts.scale.service.agreements.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ProcurementDataTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class OcdsRequirementsCsvExporter {

    private static final String CSV_HEADER = "id,templateName,criteria_id,criteria_title,requirement_group_id,requirement_group_description,OCDS_id,OCDS_title,OCDS_data_type\n";

    public String exportToCsv(Collection<ProcurementDataTemplate> dataTemplates) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(CSV_HEADER);

        ObjectMapper objectMapper = new ObjectMapper();

        for (ProcurementDataTemplate template : dataTemplates) {
            try {
                if (template.getCriteria() != null) {
                    String criteriaJson = objectMapper.writeValueAsString(template.getCriteria());
                    JsonNode criteriaNode = objectMapper.readTree(criteriaJson);
                    
                    if (criteriaNode.isArray()) {
                        for (JsonNode criterion : criteriaNode) {
                            processCriterion(template, criterion, csvBuilder);
                        }
                    } else if (criteriaNode.isObject()) {
                        processCriterion(template, criteriaNode, csvBuilder);
                    }
                }
            } catch (JsonProcessingException e) {
                log.error("Error processing criteria for template {}: {}", template.getId(), e.getMessage());
            }
        }

        return csvBuilder.toString();
    }

    private void processCriterion(ProcurementDataTemplate template, JsonNode criterion, StringBuilder csvBuilder) {
        String criteriaId = getStringValue(criterion, "id");
        String criteriaTitle = getStringValue(criterion, "title");

        JsonNode requirementGroups = criterion.get("requirementGroups");
        if (requirementGroups != null && requirementGroups.isArray()) {
            for (JsonNode requirementGroup : requirementGroups) {
                processRequirementGroup(template, criteriaId, criteriaTitle, requirementGroup, csvBuilder);
            }
        }
    }

    private void processRequirementGroup(ProcurementDataTemplate template, String criteriaId, String criteriaTitle, 
                                       JsonNode requirementGroup, StringBuilder csvBuilder) {
        // Check if this requirement group has an OCDS object
        JsonNode ocdsGroup = requirementGroup.get("OCDS");
        if (ocdsGroup != null && ocdsGroup.isObject()) {
            String requirementGroupId = getStringValue(ocdsGroup, "id");
            String requirementGroupDescription = getStringValue(ocdsGroup, "description");

            JsonNode requirements = ocdsGroup.get("requirements");
            if (requirements != null && requirements.isArray()) {
                for (JsonNode requirement : requirements) {
                    processRequirement(template, criteriaId, criteriaTitle, requirementGroupId, requirementGroupDescription, requirement, csvBuilder);
                }
            }
        }
    }

    private void processRequirement(ProcurementDataTemplate template, String criteriaId, String criteriaTitle,
                                  String requirementGroupId, String requirementGroupDescription, JsonNode requirement, StringBuilder csvBuilder) {
        JsonNode ocds = requirement.get("OCDS");
        if (ocds != null && ocds.isObject()) {
            String ocdsId = getStringValue(ocds, "id");
            String ocdsTitle = getStringValue(ocds, "title");
            String ocdsDataType = getStringValue(ocds, "dataType");

            // Escape CSV values
            String csvLine = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    escapeCsvValue(template.getId() != null ? template.getId().toString() : ""),
                    escapeCsvValue(template.getTemplateName()),
                    escapeCsvValue(criteriaId),
                    escapeCsvValue(criteriaTitle),
                    escapeCsvValue(requirementGroupId),
                    escapeCsvValue(requirementGroupDescription),
                    escapeCsvValue(ocdsId),
                    escapeCsvValue(ocdsTitle),
                    escapeCsvValue(ocdsDataType));

            csvBuilder.append(csvLine);
        }
    }

    private String getStringValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : "";
    }

    private String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        // Escape double quotes by doubling them
        return value.replace("\"", "\"\"");
    }
}
