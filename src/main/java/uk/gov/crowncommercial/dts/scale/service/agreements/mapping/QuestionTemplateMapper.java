package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.QuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementQuestionTemplate;

/**
 * MapStruct mapping definition for converting ProcurementQuestionTemplate objects to QuestionTemplate objects
 */
@Mapper(componentModel = "spring")
public interface QuestionTemplateMapper {
    @Mapping(source = "sourceModel.id", target = "templateId")
    @Mapping(source = "groupId", target = "templateGroupId")
    @Mapping(source = "sourceModel.templateName", target = "name")
    @Mapping(source = "sourceModel.description", target = "description")
    @Mapping(source = "sourceModel.mandatory", target = "mandatoryTemplate")
    @Mapping(source = "sourceModel.parent", target = "inheritsFrom")
    QuestionTemplate procurementQuestionTemplateToQuestionTemplate(ProcurementQuestionTemplate sourceModel, Integer groupId);
}