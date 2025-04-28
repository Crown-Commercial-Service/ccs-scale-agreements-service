package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementQuestionTemplate;

import java.util.Optional;

@Repository
public interface ProcurementQuestionTemplateRepo extends JpaRepository<ProcurementQuestionTemplate, Integer> {
    Optional<ProcurementQuestionTemplate> findById(Integer id);
}
