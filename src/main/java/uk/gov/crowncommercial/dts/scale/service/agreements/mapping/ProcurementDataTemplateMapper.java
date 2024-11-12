package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ProcurementDataTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementQuestionTemplate;

/**
 * MapStruct mapping definition for converting ProcurementQuestionTemplate objects to and from ProcurementDataTemplate objects
 */
@Mapper(componentModel = "spring")
public interface ProcurementDataTemplateMapper {
    @Mapping(source = "templatePayload", target = "criteria")
    ProcurementDataTemplate procurementQuestionTemplateToProcurementDataTemplate(ProcurementQuestionTemplate dbModel);
}
