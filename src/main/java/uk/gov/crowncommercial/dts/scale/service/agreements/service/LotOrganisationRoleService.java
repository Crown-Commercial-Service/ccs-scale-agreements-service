package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.ContactPointReasonRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotOrganisationRoleRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LotOrganisationRoleService {
    private final LotOrganisationRoleRepo lotOrganisationRoleRepo;



    public LotOrganisationRole createLotOrganisationRole(LotOrganisationRole lor){
        return lotOrganisationRoleRepo.saveAndFlush(lor);

    }

    public LotOrganisationRole createOrUpdateLotOrganisationRole(LotOrganisationRole updatedLor, String actionedBy){

        String dada = "";
        return lotOrganisationRoleRepo.findFirstByLotIdAndOrganisationIdAndRoleTypeId(updatedLor.getLot().getId(), updatedLor.getOrganisation().getId(), updatedLor.getRoleType().getId())
                .map(lor -> {
                    lor.setLot(updatedLor.getLot());
                    lor.setOrganisation(updatedLor.getOrganisation());
                    lor.setRoleType(updatedLor.getRoleType());
                    lor.setStartDate(updatedLor.getStartDate());
                    lor.setTradingOrganisation(updatedLor.getTradingOrganisation());
                    lor.setStartDate(updatedLor.getStartDate());
                    lor.setEndDate(updatedLor.getEndDate());
                    lor.setStatus(updatedLor.getStatus());
                    lor.setUpdatedAt(LocalDateTime.now());
                    lor.setUpdatedBy(actionedBy);
                    lotOrganisationRoleRepo.saveAndFlush(lor);
                    return lor;
        }).orElseGet(() -> {
            updatedLor.setCreatedAt(LocalDateTime.now());
            updatedLor.setCreatedBy(actionedBy);
            lotOrganisationRoleRepo.saveAndFlush(updatedLor);
            return updatedLor;
        });
    }

}