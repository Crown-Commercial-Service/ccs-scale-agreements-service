package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organization;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;


@Mapper(componentModel = "spring")
public interface ContactDetailMapper {

    @Mapping(source = "address.streetAddress", target = "streetAddress")
    @Mapping(source = "address.locality", target = "locality")
    @Mapping(source = "address.region", target = "region")
    @Mapping(source = "address.postalCode", target = "postalCode")
    @Mapping(source = "address.countryName", target = "countryName")
    @Mapping(source = "address.countryCode", target = "countryCode")
    @Mapping(source = "details.creationDate", target = "effectiveFrom")
    @Mapping(source = "contactPoint.email", target = "emailAddress")
    @Mapping(source = "contactPoint.telephone", target = "telephoneNumber")
    @Mapping(source = "contactPoint.faxNumber", target = "faxNumber")
    @Mapping(source = "contactPoint.url", target = "url")
    ContactDetail organizationToContactDetail(Organization dbModel);

}