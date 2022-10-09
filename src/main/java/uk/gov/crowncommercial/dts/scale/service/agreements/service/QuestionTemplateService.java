package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
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

    private final AgreementConverter converter;
    private final TemplateGroupRepo templateGroupRepo;

    public Collection<ProcurementDataTemplate> getDataTemplates(Lot lot, String eventType){
        Set<TemplateGroup> groups = templateGroupRepo.findByLotIdAndEventTypeName(lot.getId(), eventType);
        Set<ProcurementQuestionTemplate> templates = new HashSet<>();
        if(groups.size() > 0){
            for(TemplateGroup tg : groups){
                templates.addAll(tg.getQuestionTemplates());
            }
        }

        if(templates.size() > 0){
            return templates.stream().map(converter::getDataTemplate).collect(Collectors.toSet());
        }else{
            return converter.convertLotProcurementQuestionTemplateToDataTemplates(
                    lot.getProcurementQuestionTemplates(), eventType);
        }
    }
}
