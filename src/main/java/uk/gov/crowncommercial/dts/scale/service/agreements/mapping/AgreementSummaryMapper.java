package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;

/**
 * MapStruct mapping definition for converting CommercialAgreement objects to and from AgreementSummary objects
 */
@Mapper(componentModel = "spring")
public interface AgreementSummaryMapper {
    AgreementSummary commercialAgreementToAgreementSummary(CommercialAgreement dbModel);
    CommercialAgreement agreementSummaryToCommercialAgreement(AgreementSummary outputModel);
}
