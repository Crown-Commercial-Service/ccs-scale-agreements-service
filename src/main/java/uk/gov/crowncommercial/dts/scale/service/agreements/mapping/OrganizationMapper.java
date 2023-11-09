package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organization;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Scheme;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "legalName", target = "name")
    Organization OrganisationToOrganization(Organisation dbModel);

    @Mapping(source = "identifier.scheme", target = "registryCode", qualifiedByName = "registryCodeToOrgScheme")
    @Mapping(source = "identifier.id", target = "entityId")
    @Mapping(source = "identifier.legalName", target = "legalName")
    @Mapping(source = "identifier.uri", target = "uri")
    @Mapping(source = "details.creationDate", target = "incorporationDate")
    @Mapping(source = "details.countryCode", target = "incorporationCountry")
    @Mapping(source = "address.countryName", target = "countryName")
    @Mapping(source = "details.companyType", target = "businessType")
    @Mapping(source = "details.status", target = "status")
    @Mapping(source = "details.isSme", target = "isSme")
    @Mapping(source = "details.isVcse", target = "isVcse")
    @Mapping(source = "details.active", target = "isActive")
    Organisation OrganizationToOrganisation(Organization dbModel);


    @Named("registryCodeToOrgScheme")
    public static String OrgSchemeToRegistryCode(String orgScheme) {
        if (orgScheme != null && !orgScheme.isEmpty()) {
            return Enum.valueOf(Scheme.class, orgScheme.toUpperCase()).getName();
        }
        return null;
    }


}
