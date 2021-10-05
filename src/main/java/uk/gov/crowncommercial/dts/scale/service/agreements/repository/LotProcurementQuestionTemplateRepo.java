package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementQuestionTemplate;

/**
 * Commercial Agreement Data Repository.
 */
@Repository
public interface LotProcurementQuestionTemplateRepo
    extends JpaRepository<LotProcurementQuestionTemplate, Integer> {

  Collection<LotProcurementQuestionTemplate> findByLotAgreementNumberAndLotNumberAndProcurementEventTypeName(
      String agreementNumber, String lotNumber, String eventType);
}
