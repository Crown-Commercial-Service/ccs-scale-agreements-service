package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MapStruct mapping definition for converting LotOrganisationRole objects to and from LotSupplier objects
 */
@Mapper(componentModel = "spring")
public abstract class LotSupplierMapper {
    AgreementDetailMapper AGREEMENTMAPPERINSTANCE = Mappers.getMapper(AgreementDetailMapper.class);

    @Mapping(target = "organization", expression = "java(lotOrganisationRoleToOrganization(dbModel))" )
    @Mapping(target = "supplierStatus", expression = "java(organisationToSupplierStatus(dbModel.getOrganisation()))")
    @Mapping(target = "lotContacts", expression = "java(contactPointLotOrgRolesToContacts(dbModel.getContactPointLotOrgRoles()))" )
    public abstract LotSupplier lotOrganisationRoleToLotSupplier(LotOrganisationRole dbModel);

    @Mapping(source = "incorporationDate", target = "creationDate")
    @Mapping(source = "incorporationCountry", target = "countryCode")
    @Mapping(source = "businessType", target = "companyType")
    @Mapping(source = "isActive", target = "active")
    public abstract OrganizationDetail organisationToOrganizationDetail(Organisation orgModel);

    @Named("registryCodeToOrgScheme")
    public static Scheme registryCodeToOrgScheme(String registryCode) {
        if (registryCode != null && !registryCode.isEmpty()) {
            String regex = "([a-z])([A-Z]+)";
            String replacement = "$1_$2";
            registryCode.replaceAll(regex, replacement);

            return Enum.valueOf(Scheme.class, registryCode.toUpperCase().replace("-", ""));
        }

        return null;
    }

    @Mapping(target="id", source="orgModel.entityId")
    @Mapping(source = "registryCode", target = "scheme", qualifiedByName = "registryCodeToOrgScheme")
    public abstract OrganizationIdentifier organisationToOrganizationIdentifier(Organisation orgModel);

    @Mapping(target="name", source="lotOrgRole.contactPointName")
    @Mapping(target="email", source="contactDetail.emailAddress")
    @Mapping(target="telephone", source="contactDetail.telephoneNumber")
    @Mapping(target="faxNumber", source="contactDetail.faxNumber")
    @Mapping(target="url", source="contactDetail.url")
    public abstract ContactPoint sourcesToContactPoint(ContactPointLotOrgRole lotOrgRole, ContactDetail contactDetail);

    @Named("lotOrganisationRoleToOrganization")
    public Organization lotOrganisationRoleToOrganization(LotOrganisationRole orgRole) {
        Organization model = new Organization();

        Organisation sourceOrg = orgRole.getOrganisation();
        Set<ContactPointLotOrgRole> sourceContactRoles = orgRole.getContactPointLotOrgRoles();

        if (sourceOrg != null) {
            model.setName(sourceOrg.getLegalName());
            model.setRoles(Collections.singleton(PartyRole.SUPPLIER));
            model.setDetails(organisationToOrganizationDetail(sourceOrg));
            model.setId((sourceOrg.getRegistryCode() == null ? "SCHEME-UNKNOWN" : sourceOrg.getRegistryCode()) + "-" + (sourceOrg.getEntityId() == null ? "ID-UNKNOWN" : sourceOrg.getEntityId()));
            model.setIdentifier(organisationToOrganizationIdentifier(sourceOrg));

            if (sourceContactRoles != null) {
                sourceContactRoles.stream().filter(ContactPointLotOrgRole::getPrimary).findFirst()
                        .ifPresent(cplor -> {
                            model.setContactPoint(sourcesToContactPoint(cplor, cplor.getContactDetail()));
                            model.setAddress(AGREEMENTMAPPERINSTANCE.contactDetailToAddress(cplor.getContactDetail()));
                        });
            }

            if (orgRole.getTradingOrganisation() != null && model.getDetails() != null) {
                model.getDetails().setTradingName(orgRole.getTradingOrganisation().getTradingOrganisationName());
            }

            return model;
        }

        return null;
    }

    @Named("organisationToSupplierStatus")
    public SupplierStatus organisationToSupplierStatus(Organisation orgModel) {
        if (orgModel != null && !orgModel.getStatus().isEmpty()) {
            return Enum.valueOf(SupplierStatus.class, orgModel.getStatus().toUpperCase().replace('-', '_'));
        }

        return null;
    }

    @Named("contactPointLotOrgRolesToContacts")
    public Set<Contact> contactPointLotOrgRolesToContacts(Set<ContactPointLotOrgRole> orgRoles) {
        if (orgRoles != null) {
            return orgRoles.stream().map(cplor -> {
                Contact contact = new Contact();
                contact.setContactReason(cplor.getContactPointReason().getName());
                contact.setContactPoint(sourcesToContactPoint(cplor, cplor.getContactDetail()));
                return contact;
            }).collect(Collectors.toSet());
        }

        return null;
    }
}
