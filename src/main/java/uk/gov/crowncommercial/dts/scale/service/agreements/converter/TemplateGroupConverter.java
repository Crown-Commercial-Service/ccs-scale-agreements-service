package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.QuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementQuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.TemplateGroup;

import java.util.*;

public class TemplateGroupConverter {
    public void assignTemplates(Lot lot, Collection<EventType> eventTypes) {
        Map<String, EventType> eventTypeMap = new HashMap<>();
        for (EventType e : eventTypes) {
            eventTypeMap.put(e.getType(), e);
        }

        Collection<TemplateGroup> templateGroups = lot.getTemplateGroups();
        for (TemplateGroup tg : templateGroups) {
            assignTemplates(tg, eventTypeMap);
        }
    }

    public void assignTemplates(TemplateGroup tg, Map<String, EventType> eventTypeMap) {
        Collection<ProcurementQuestionTemplate> questionTemplates = tg.getQuestionTemplates();
        if (questionTemplates.size() > 0) {
            String type = tg.getEventType().getName();
            EventType eventType = eventTypeMap.get(type);
            if (null != eventType) {
                if(null == eventType.getTemplateGroups())
                    eventType.setTemplateGroups(new ArrayList<>());
                for(ProcurementQuestionTemplate questionTemplate : questionTemplates){
                    QuestionTemplate template = new QuestionTemplate();
                    template.setTemplateId(questionTemplate.getId());
                    template.setTemplateGroupId(tg.getId());
                    template.setName(questionTemplate.getTemplateName());
                    template.setDescription(questionTemplate.getDescription());
                    template.setMandatoryTemplate(questionTemplate.getMandatory());
                    template.setInheritsFrom(questionTemplate.getParent());
                    eventType.getTemplateGroups().add(template);
                }
            }
        }
    }
}
