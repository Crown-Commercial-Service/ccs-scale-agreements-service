package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;

import java.util.*;
import java.util.stream.Collectors;

import static uk.gov.crowncommercial.dts.scale.service.agreements.config.Constants.OCDS_ROLE_FRAMEWORK_OWNER;

/**
 * MapStruct mapping definition for converting CommercialAgreement objects to and from AgreementDetail objects
 */
@Mapper(componentModel = "spring")
public interface AgreementDetailMapper {
    AgreementDetailMapper INSTANCE = Mappers.getMapper(AgreementDetailMapper.class);

    @Mapping(source = "organisationRoles", target = "contacts", qualifiedByName = "orgRolesToContacts")
    @Mapping(source = "benefits", target = "benefits", qualifiedByName = "commercialAgreementBenefitsToStrings")
    @Mapping(source = "lots", target = "lots", qualifiedByName = "lotsToLotSummaries")
    @Mapping(source = "organisationRoles", target = "owner", qualifiedByName = "orgRolesToOrganization")
    AgreementDetail commercialAgreementToAgreementDetail(CommercialAgreement dbModel);

    @Mapping(source = "benefits", target = "benefits", qualifiedByName = "stringsToCommercialAgreementBenefits")
    @Mapping(source = "ownerName", target = "owner")
    CommercialAgreement agreementDetailToCommercialAgreement(AgreementDetail dbModel);

    // Mapping of child entities starts here
    // Lots
    @Named("lotsToLotSummaries")
    Collection<LotSummary> lotsToLotSummaries(Set<Lot> dbLots);


    // Contacts
    @Mapping(source = "caOrgRole.contactPointName", target = "name")
    @Mapping(source = "contactDetail.emailAddress", target = "email")
    @Mapping(source = "contactDetail.telephoneNumber", target = "telephone")
    @Mapping(source = "contactDetail.faxNumber", target = "faxNumber")
    @Mapping(source = "contactDetail.url", target = "url")
    ContactPoint sourcesToContactPoint(ContactPointCommercialAgreementOrgRole caOrgRole, ContactDetail contactDetail);

    @Mapping(source = "contactPoint", target = "contactPoint")
    @Mapping(source = "contactReason.name", target = "contactReason")
    Contact sourcesToContact(ContactPoint contactPoint, ContactPointReason contactReason);

    @Named("orgRolesToContacts")
    public static Collection<Contact> orgRolesToContacts(Set<CommercialAgreementOrgRole> orgRoles) {
        // This is a complex mapping - we need to map each CommercialAgreementOrgRole to sub-objects which we then combine into a Contact
        if (orgRoles != null) {
            return orgRoles.stream()
                    .flatMap(caor -> caor.getContactPointCommercialAgreementOrgRoles().stream()).map(cpcaor -> {
                        final ContactDetail contactDetail = cpcaor.getContactDetail();

                        ContactPoint contactPoint = INSTANCE.sourcesToContactPoint(cpcaor, contactDetail);

                        return INSTANCE.sourcesToContact(contactPoint, cpcaor.getContactPointReason());
                    }).collect(Collectors.toSet());
        }

        return null;
    }


    // Benefits
    @Named("commercialAgreementBenefitsToStrings")
    public static List<String> commercialAgreementBenefitsToStrings(Collection<CommercialAgreementBenefit> benefits) {
        List<String> model = new ArrayList<>();

        if (benefits != null) {
            model = benefits.stream().sorted(Comparator.comparingInt(CommercialAgreementBenefit::getSequence))
                            .map(CommercialAgreementBenefit::getDescription).collect(Collectors.toList());
        }

        return model;
    }

    // Benefits from String (JSON)
    @Named("stringsToCommercialAgreementBenefits")
    public static Set<CommercialAgreementBenefit> stringsToCommercialAgreementBenefits(Collection<String> updateBenefits) {

        if (updateBenefits != null) {
            String[] updateBenefitsList = updateBenefits.toArray(new String[0]);
            Set<CommercialAgreementBenefit> benefits = new LinkedHashSet<CommercialAgreementBenefit>();

            for (int i = 0; i < updateBenefitsList.length; i++) {
                CommercialAgreementBenefit cab = new CommercialAgreementBenefit();
                cab.setName(updateBenefitsList[i]);
                cab.setDescription(updateBenefitsList[i]);
                cab.setSequence(i+1);

                benefits.add(cab);
            }
            return benefits;
        }

        return null;
    }

    // Organisation
    Address contactDetailToAddress(ContactDetail contactDetail);

    @Mapping(target="id", source="orgModel.entityId")
    @Mapping(source = "registryCode", target = "scheme", qualifiedByName = "registryCodeToScheme")
    OrganizationIdentifier orgToOrganizationIdentifier(Organisation orgModel);

    @Named("registryCodeToScheme")
    public static Scheme registryCodeToScheme(String registryCode) {
        if (registryCode != null && !registryCode.isEmpty()) {
            String regex = "([a-z])([A-Z]+)";
            String replacement = "$1_$2";
            registryCode.replaceAll(regex, replacement);

            return Enum.valueOf(Scheme.class, registryCode.toUpperCase().replace('-', '_'));
        }

        return null;
    }

    @Mapping(source = "orgModel.incorporationDate", target = "creationDate")
    @Mapping(source = "orgModel.incorporationCountry", target = "countryCode")
    @Mapping(source = "orgModel.businessType", target = "companyType")
    @Mapping(source = "orgModel.isActive", target = "active")
    OrganizationDetail orgToOrgDetail(Organisation orgModel);

    @Named("orgRolesToOrganization")
    public static Organization orgRolesToOrganization(Set<CommercialAgreementOrgRole> orgRoles) {
        if (orgRoles != null) {
            Optional<CommercialAgreementOrgRole> optionalOrgRole = orgRoles.stream()
                    .filter(c -> c.getRoleType() != null
                            && Objects.equals(OCDS_ROLE_FRAMEWORK_OWNER, c.getRoleType().getName()))
                    .findFirst();

            if (optionalOrgRole.isPresent()) {
                CommercialAgreementOrgRole orgRole = optionalOrgRole.get();
                Organisation dbOrgModel = orgRole.getOrganisation();
                Organization model = new Organization();

                final Optional<ContactPointCommercialAgreementOrgRole> optionalContactPoint = orgRole.getContactPointCommercialAgreementOrgRoles().stream().findFirst();

                if (optionalContactPoint.isPresent()) {
                    ContactPointCommercialAgreementOrgRole cpOrgRole = optionalContactPoint.get();
                    model.setContactPoint(INSTANCE.sourcesToContactPoint(cpOrgRole, cpOrgRole.getContactDetail()));
                    model.setAddress(INSTANCE.contactDetailToAddress(cpOrgRole.getContactDetail()));
                }

                model.setName(dbOrgModel.getLegalName());
                model.setId((dbOrgModel.getRegistryCode() == null ? "SCHEME-UNKNOWN" : dbOrgModel.getRegistryCode()) + "-"
                        + (dbOrgModel.getEntityId() == null ? "ID-UNKNOWN" : dbOrgModel.getEntityId()));
                model.setIdentifier(INSTANCE.orgToOrganizationIdentifier(dbOrgModel));
                model.setDetails(INSTANCE.orgToOrgDetail(dbOrgModel));
                model.setRoles(Collections.singleton(PartyRole.FRAMEWORK_OWNER));

                return model;
            }
        }

        return null;
    }
}
