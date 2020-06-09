package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

/**
*
*/
@Repository
public interface CommercialAgreementRepo extends JpaRepository<CommercialAgreement, UUID> {

}
