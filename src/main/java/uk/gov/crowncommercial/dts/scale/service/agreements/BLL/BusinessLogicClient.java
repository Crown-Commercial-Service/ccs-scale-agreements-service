package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Serves as an intermediary level between the Controllers and the Services, where we can retrieve, format and cache data appropriately
 * Everything in here should be cached
 */
@Component
public class BusinessLogicClient {
    @Autowired
    private AgreementService agreementService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private WordpressService wordpressService;

    @Autowired
    private QuestionTemplateService questionTemplateService;

    @Autowired
    protected MappingService mappingService;

    /**
     * Returns a list of all Commercial Agreements
     */
    @Cacheable(value = "getAgreementsList", key = "#root.methodName")
    public List<AgreementSummary> getAgreementsList() {
        // Fetch the list of agreements from the service
        final List<CommercialAgreement> agreements = agreementService.getAgreements();

        // Now convert the list to the objects we want to return
        return agreements.stream().map(mappingService::mapCommercialAgreementToAgreementSummary).collect(Collectors.toList());
    }

    /**
     * Returns a specific AgreementDetail object based on a requested ID
     */
    @Cacheable(value = "getAgreementDetail")
    public AgreementDetail getAgreementDetail(String agreementId) {
        AgreementDetail model = null;

        // Fetch the Commercial Agreement from the service
        CommercialAgreement agreementModel = agreementService.findAgreementByNumber(agreementId);

        if (agreementModel != null) {
            // We now need to enhance the agreement model we've retrieved with data from the Wordpress API, so do that
            agreementModel = wordpressService.getExpandedCommercialAgreement(agreementModel, agreementId);

            // Now we should have a complete agreement model - convert it to the format we desire, and then return it
            model = mappingService.mapCommercialAgreementToAgreementDetail(agreementModel);
        }

        return model;
    }

    /**
     * Returns a list of LotDetail objects which relate to a specified agreement
     */
    @Cacheable(value = "getLotsForAgreement", key="#agreementId + #buyingMethod.toString()")
    public Collection<LotDetail> getLotsForAgreement(String agreementId, BuyingMethod buyingMethod) {
        Collection<LotDetail> model = null;

        // Fetch the commercial agreement from the service
        CommercialAgreement agreementModel = agreementService.findAgreementByNumber(agreementId);

        if (agreementModel != null) {
            // Now convert the lots specified in the agreement into the format we want to return
            model = agreementModel.getLots().stream().map(mappingService::mapLotToLotDetail).collect(Collectors.toList());

            // Finally, if we're given a buying method we need to filter our results to just those matching the method specified
            if (buyingMethod != null && buyingMethod != BuyingMethod.NONE) {
                model = model.stream()
                        .filter(lot -> lot.getRoutesToMarket().stream()
                                .filter(route -> buyingMethod == route.getBuyingMethod()).count() > 0)
                        .collect(Collectors.toSet());
            }
        }

        return model;
    }

    /**
     * Returns a list of Document objects which relate to a specified agreement
     */
    @Cacheable(value = "getDocumentsForAgreement")
    public Collection<Document> getDocumentsForAgreement(String agreementId) {
        Collection<Document> model = null;

        // Fetch the commercial agreement from the service
        CommercialAgreement agreementModel = agreementService.findAgreementByNumber(agreementId);

        if (agreementModel != null && agreementModel.getDocuments() != null) {
            // Now convert the documents specified in the agreement into the format we want to return
            model = agreementModel.getDocuments().stream().map(mappingService::mapCommercialAgreementDocumentToDocument).collect(Collectors.toList());
        }

        return model;
    }

    /**
     * Returns a list of AgreementUpdate objects which relate to a specified agreement
     */
    @Cacheable(value = "getUpdatesForAgreement")
    public Collection<AgreementUpdate> getUpdatesForAgreement(String agreementId) {
        Collection<AgreementUpdate> model = null;

        // Fetch the commercial agreement from the service
        CommercialAgreement agreementModel = agreementService.findAgreementByNumber(agreementId);

        if (agreementModel != null) {
            // Now convert the updates specified in the agreement into the format we want to return
            model = agreementModel.getUpdates().stream().map(mappingService::mapCommercialAgreementUpdateToAgreementUpdate).collect(Collectors.toList());
        }

        return model;
    }

    /**
     * Returns a specific LotDetail object based on requested agreement and lot IDs
     */
    @Cacheable(value = "getLotDetail", key="#agreementId + #lotId")
    public LotDetail getLotDetail(String agreementId, String lotId) {
        LotDetail model = new LotDetail();

        // Fetch the lot from the service
        Lot lotModel = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);

        if (lotModel != null) {
            // We now need to enhance the lot model we've retrieved with data from the Wordpress API, so do that
            lotModel = wordpressService.getExpandedLot(lotModel, agreementId, lotId);

            // Now we should have a complete lot model - convert it to the format we desire, and then return it
            model = mappingService.mapLotToLotDetail(lotModel);
        }

        return model;
    }

    /**
     * Returns a list of LotSupplier objects which relate to a specified lot / agreement
     */
    @Cacheable(value = "getLotSuppliers", key="#agreementId + #lotId")
    public Collection<LotSupplier> getLotSuppliers(String agreementId, String lotId) {
        Collection<LotSupplier> model = null;

        // Fetch a list of LotOrganisationRoles from the service
        Collection<LotOrganisationRole> lotOrgRoles = agreementService.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(agreementId, lotId);

        if (lotOrgRoles != null) {
            // Now convert the items we've found into the format we want to return
            model = lotOrgRoles.stream().map(mappingService::mapLotOrganisationRoleToLotSupplier).collect(Collectors.toList());
        }

        return model;
    }

    /**
     * Returns a list of EventType objects which relate to a specified lot / agreement
     */
    @Cacheable(value = "getLotEventTypes", key="#agreementId + #lotId")
    public Collection<EventType> getLotEventTypes(String agreementId, String lotId) {
        Collection<EventType> model;

        // Fetch the lot from the service
        Lot lotModel = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);

        if (lotModel != null && lotModel.getProcurementEventTypes() != null) {
            // Now use the lot object to generate our list of EventType
            model = lotModel.getProcurementEventTypes().stream().map(lotEventType -> mappingService.mapLotProcurementEventTypeToEventType(lotEventType, lotModel)).collect(Collectors.toList());

            // Before we finish we also need to assign the relevant template groups to our results
            Collection<TemplateGroup> templateGroups = lotModel.getTemplateGroups();

            if (templateGroups != null && !templateGroups.isEmpty()) {
                // We've found template groups, so now for each one we've found get the matching Event Type and then map the question templates to it
                templateGroups.forEach(group -> {
                    Collection<ProcurementQuestionTemplate> questionTemplates = group.getQuestionTemplates();

                    if (questionTemplates != null && !questionTemplates.isEmpty()) {
                        EventType matchingType = model.stream().filter(result -> result.getType().equals(group.getEventType().getName())).findFirst().orElse(null);

                        if (matchingType != null) {
                            // We've found the event type to match the data with - now just directly map it
                            if (matchingType.getTemplateGroups() == null) {
                                matchingType.setTemplateGroups(new ArrayList<>());
                            }

                            questionTemplates.forEach(qt -> {
                                matchingType.getTemplateGroups().add(mappingService.mapProcurementQuestionTemplateToQuestionTemplate(qt, group.getId()));
                            });
                        }
                    }
                });
            }
        } else {
            model = null;
        }

        return model;
    }

    /**
     * Returns a list of ProcurementDataTemplate objects which relate to a specified event
     */
    @Cacheable(value = "getEventDataTemplates", key="#agreementId + #lotId + #eventType")
    public Collection<ProcurementDataTemplate> getEventDataTemplates(String agreementId, String lotId, String eventType) {
        Collection<ProcurementDataTemplate> model = null;

        // Fetch the lot from the service
        Lot lotModel = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);

        if (lotModel != null) {
            // Now use the let to fetch the data templates from the template service.  Mapping is performed within the service for this one
            model = questionTemplateService.getDataTemplates(lotModel, eventType);
        }

        return model;
    }

    /**
     * Returns a list of Document objects which relate to a specified event
     */
    @Cacheable(value = "getEventDocumentTemplates", key="#agreementId + #lotId + #eventType")
    public Collection<Document> getEventDocumentTemplates(String agreementId, String lotId, String eventType) {
        Collection<Document> model = null;

        // Fetch the lot from the service
        Lot lotModel = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);

        if (lotModel != null && lotModel.getProcurementQuestionTemplates() != null) {
            // Now use the lot to convert the procurement question templates to documents
            model = lotModel.getProcurementQuestionTemplates().stream().filter(t -> t.getProcurementQuestionTemplate().getTemplateUrl() != null
                    && eventType.equalsIgnoreCase(t.getProcurementEventType().getName()))
                    .map(template -> mappingService.mapLotProcurementQuestionTemplateToDocument(template.getProcurementQuestionTemplate())).collect(Collectors.toList());
        }

        return model;
    }

    /**
     * Returns the new agreement or updated agreement from the api call
     */
    public AgreementDetail saveAgreement(AgreementDetail cd, String agreementNumber){
        CommercialAgreement ca = mappingService.mapAgreementDetailToCommercialAgreement(cd);
        ca.setNumber(agreementNumber);
        ca.isValid();

        return mappingService.mapCommercialAgreementToAgreementDetail(agreementService.createOrUpdateAgreement(ca));
    }

    /**
     * Returns the new lot or updated lot from the api call
     */
    public LotDetail saveLot(LotDetail ld, String agreementNumber, String lotNumber){

        Lot lot = mappingService.mapLotDetailToLot(ld);
        lot.setAgreement(agreementService.findAgreementByNumber(agreementNumber));
        lot.setNumber(lotNumber);
        lot.isValid();

        return mappingService.mapLotToLotDetail(agreementService.createOrUpdateLot(lot));
    }

    /**
     * batch create or update the lot details that was given and return it
     * Nothing will write to the DB until it verify that all lots are valid
     */
    public Collection<LotDetail> saveLots(Collection<LotDetail> lotDetailSet, String agreementNumber){

        Collection<Lot> lotCollection = new HashSet<Lot>();

        lotDetailSet.forEach(lotDetail -> {
            Lot lot = mappingService.mapLotDetailToLot(lotDetail);
            lot.setAgreement(agreementService.findAgreementByNumber(agreementNumber));
            lot.isValid();
            lotCollection.add(lot);
        });

        return lotCollection.stream().map(lot -> { return mappingService.mapLotToLotDetail(agreementService.createOrUpdateLot(lot));}).collect(Collectors.toSet());
    }

    /**
     * batch create or update a list of LotSupplier objects that was given and return it
     */
    public Collection<LotSupplier> saveLotSuppliers(String agreementId, String lotNumber,  Set<LotSupplier> lotSuppliersSet) {
        Lot lot = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotNumber);

        lotSuppliersSet.forEach(lotDetail ->{
            final Organisation organisation = mappingService.mapLotSupplierToOrganisation(lotDetail);
            organisation.isValid();

            ContactDetail contactDetail = mappingService.mapLotSupplierToContactDetail(lotDetail);
            contactDetail = contactDetail.isValid() ? contactDetail : null;

            supplierService.addSupplierRelationship(lot, organisation, contactDetail, lotDetail.getLastUpdatedBy(), lotDetail.getSupplierStatus());
        });

        return getLotSuppliers(agreementId, lotNumber);
    }
}
