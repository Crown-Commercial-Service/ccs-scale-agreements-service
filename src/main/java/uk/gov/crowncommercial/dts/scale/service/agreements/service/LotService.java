package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;


@Service
@RequiredArgsConstructor
@Slf4j
public class LotService {
    private final LotRepo lotRepo;

    public Lot findLotSupplierOrgRolesByAgreementNumberAndLotNumber(final String agreementNumber, final String lotNumber) {
        return lotRepo.findByAgreementNumberAndNumber(agreementNumber,lotNumber);
    }

}
