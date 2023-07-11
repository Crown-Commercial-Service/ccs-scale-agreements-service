package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ProcurementDataTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementQuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.TemplateGroup;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.TemplateGroupRepo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionTemplateService {
    @Autowired
    protected MappingService mappingService;

    private final TemplateGroupRepo templateGroupRepo;

    public Collection<ProcurementDataTemplate> getDataTemplates(Lot lot, String eventType) {
        Set<TemplateGroup> groups = templateGroupRepo.findByLotIdAndEventTypeName(lot.getId(), eventType);
        Set<ProcurementQuestionTemplate> templates = new HashSet<>();

        if (groups != null && !groups.isEmpty()) {
            for (TemplateGroup tg : groups) {
                templates.addAll(tg.getQuestionTemplates());
            }
        }

        if (templates != null && !templates.isEmpty()) {
            return templates.stream().map(mappingService::mapProcurementQuestionTemplateToProcurementDataTemplate).collect(Collectors.toSet());
        } else {
            if (lot.getProcurementQuestionTemplates() != null && !lot.getProcurementQuestionTemplates().isEmpty()) {
                return lot.getProcurementQuestionTemplates().stream().filter(t -> t.getProcurementQuestionTemplate().getTemplatePayload() != null
                        && eventType.equalsIgnoreCase(t.getProcurementEventType().getName()))
                        .map(t -> mappingService.mapProcurementQuestionTemplateToProcurementDataTemplate(t.getProcurementQuestionTemplate()))
                        .collect(Collectors.toSet());
            }
        }

        return null;
    }
}
