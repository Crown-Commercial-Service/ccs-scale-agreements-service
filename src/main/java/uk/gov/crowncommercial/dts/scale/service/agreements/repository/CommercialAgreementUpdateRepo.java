package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;

/**
 * Commercial Agreement Update Data Repository.
 */
@Repository
public interface CommercialAgreementUpdateRepo
    extends JpaRepository<CommercialAgreementUpdate, Integer> {

  Collection<CommercialAgreementUpdate> findByAgreementNumber(String caNumber);
}
