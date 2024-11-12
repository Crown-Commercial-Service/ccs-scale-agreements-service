package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;

/**
 * MapStruct mapping definition for converting CommercialAgreementUpdate objects to and from AgreementUpdate objects
 */
@Mapper(componentModel = "spring")
public interface AgreementUpdateMapper {
    @Mapping(source = "publishedDate", target = "date")
    @Mapping(source = "url", target = "linkUrl")
    @Mapping(source = "description", target = "text")
    AgreementUpdate commercialAgreementUpdateToAgreementUpdate(CommercialAgreementUpdate dbModel);
}
