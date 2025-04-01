package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ProcurementDataTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementQuestionTemplate;

/**
 * MapStruct mapping definition for converting ProcurementQuestionTemplate objects to and from ProcurementDataTemplate objects
 */
@Mapper(componentModel = "spring")
public interface ProcurementDataTemplateMapper {
    @Mapping(source = "templatePayload", target = "criteria", qualifiedByName = "jsonToObject")
    ProcurementDataTemplate procurementQuestionTemplateToProcurementDataTemplate(ProcurementQuestionTemplate dbModel);

    @Named("jsonToObject")
    static Object jsonToObject(Object sourceJson) throws JsonProcessingException {
        // We just want to deserialize the JSON string we have into an object
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(sourceJson.toString(), Object.class);
        } catch (Exception ex) {
            // Fail silently
        }

        return null;
    }
}