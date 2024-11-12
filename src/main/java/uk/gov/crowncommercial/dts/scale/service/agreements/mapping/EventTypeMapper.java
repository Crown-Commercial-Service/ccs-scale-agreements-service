package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.QuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MapStruct mapping definition for converting LotProcurementEventType objects to and from EventType objects
 */
@Mapper(componentModel = "spring")
public abstract class EventTypeMapper {
    @Mapping(source = "dbModel.assessmentToolId", target = "assessmentToolId")
    @Mapping(source = "dbModel.isMandatoryEvent", target = "mandatoryEventInd")
    @Mapping(target = "description", expression = "java(procurementEventTypeToDescription(dbModel.getProcurementEventType()))" )
    @Mapping(target = "type", expression = "java(procurementEventTypeToType(dbModel.getProcurementEventType()))" )
    @Mapping(target = "preMarketActivity", expression = "java(procurementEventTypeToPreMarketActivity(dbModel.getProcurementEventType()))" )
    @Mapping(target = "templateGroups", expression = "java(templateGroupsToQuestionTemplates(lotModel.getTemplateGroups()))" )
    public abstract EventType lotProcurementEventTypeToEventType(LotProcurementEventType dbModel, Lot lotModel);

    @Named("procurementEventTypeToDescription")
    public String procurementEventTypeToDescription(ProcurementEventType sourceModel) {
        if (sourceModel != null) {
            return sourceModel.getDescription();
        }

        return null;
    }

    @Named("procurementEventTypeToType")
    public String procurementEventTypeToType(ProcurementEventType sourceModel) {
        if (sourceModel != null) {
            return sourceModel.getName();
        }

        return null;
    }

    @Named("procurementEventTypeToPreMarketActivity")
    public Boolean procurementEventTypeToPreMarketActivity(ProcurementEventType sourceModel) {
        if (sourceModel != null) {
            return sourceModel.getPreMarketActivity();
        }

        return null;
    }

    @Named("procurementQuestionTemplateToQuestionTemplate")
    @Mapping(source = "questionTemplate.id", target = "templateId")
    @Mapping(source = "templateGroup.id", target = "templateGroupId")
    @Mapping(source = "questionTemplate.templateName", target = "name")
    @Mapping(source = "questionTemplate.description", target = "description")
    @Mapping(source = "questionTemplate.mandatory", target = "mandatoryTemplate")
    @Mapping(source = "questionTemplate.parent", target = "inheritsFrom")
    public abstract QuestionTemplate procurementQuestionTemplateToQuestionTemplate(TemplateGroup templateGroup, ProcurementQuestionTemplate questionTemplate);

    @Named("templateGroupsToQuestionTemplates")
    public Collection<QuestionTemplate> templateGroupsToQuestionTemplates(Collection<TemplateGroup> templateGroups) {
        if (templateGroups != null && !templateGroups.isEmpty()) {
            templateGroups.forEach(templateGroup -> {
                Collection<ProcurementQuestionTemplate> questionTemplates = templateGroup.getQuestionTemplates();

                if (questionTemplates != null && !questionTemplates.isEmpty()) {
                    Collection<QuestionTemplate> model =  new ArrayList<>();

                    questionTemplates.forEach(template -> {
                        model.add(procurementQuestionTemplateToQuestionTemplate(templateGroup, template));
                    });
                }
            });
        }

        return null;
    }
}
