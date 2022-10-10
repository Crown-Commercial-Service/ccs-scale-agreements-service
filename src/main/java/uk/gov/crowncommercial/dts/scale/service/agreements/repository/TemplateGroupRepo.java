package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.TemplateGroup;

import java.util.Set;

public interface TemplateGroupRepo extends JpaRepository<TemplateGroup, Integer> {
    Set<TemplateGroup> findByLotIdAndEventTypeName(Integer lotId, String eventType);
}
