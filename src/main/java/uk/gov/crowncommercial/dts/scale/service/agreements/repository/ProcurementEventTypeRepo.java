package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementEventType;

/**
 * Event Types Data Repository.
 */
@Repository
public interface ProcurementEventTypeRepo extends JpaRepository<ProcurementEventType, Integer> {

    ProcurementEventType findByName(String procurementEventTypeName);

}
