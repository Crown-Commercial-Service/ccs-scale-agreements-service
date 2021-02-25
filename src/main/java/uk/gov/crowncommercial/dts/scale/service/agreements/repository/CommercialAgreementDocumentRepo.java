package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;

/**
 * Commercial Agreement Document Data Repository.
 */
@Repository
public interface CommercialAgreementDocumentRepo
    extends JpaRepository<CommercialAgreementDocument, Integer> {

  Collection<CommercialAgreementDocument> findByAgreementNumber(String caNumber);
}
