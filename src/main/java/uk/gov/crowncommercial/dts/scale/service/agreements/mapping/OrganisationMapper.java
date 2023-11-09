package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationDetail;


/**
 * MapStruct mapping definition for converting Organisation objects to and from OrganizationDetail objects
 */

@Mapper(componentModel = "spring")
public interface OrganisationMapper {
    @Mapping(source = "legalName", target = "tradingName")
    @Mapping(source = "incorporationDate", target = "creationDate")
    @Mapping(source = "incorporationCountry", target = "countryCode")
    @Mapping(source = "registryCode", target = "dunsNumber")
    @Mapping(source = "uri", target = "uri")
    @Mapping(source = "id", target = "id")


    OrganizationDetail OrganisationToOrganizationDetail(Organisation dbModel);
}