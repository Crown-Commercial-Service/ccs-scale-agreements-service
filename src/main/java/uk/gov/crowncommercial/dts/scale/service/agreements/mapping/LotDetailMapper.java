package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MapStruct mapping definition for converting Lot objects to and from LotDetail objects
 */
@Mapper(componentModel = "spring")
public abstract class LotDetailMapper {
    @Autowired
    protected AgreementService agreementService;

    @Mapping(source = "lotType", target = "type", qualifiedByName = "stringToLotType")
    @Mapping(source = "sectors", target = "sectors", qualifiedByName = "sectorsToStrings")
    @Mapping(source = "relatedAgreementLots", target = "relatedAgreementLots", qualifiedByName = "lotRelatedLotToRelatedAgreementLots")
    @Mapping(source = "rules", target = "rules", qualifiedByName = "lotRulesToLotRulesDto")
    @Mapping(source = "routesToMarket", target = "routesToMarket", qualifiedByName = "lotRouteToMarketsToRouteToMarketDtos")
    public abstract LotDetail lotToLotDetail(Lot dbModel);

    // Mapping of child entities starts here
    // Type
    @Named("stringToLotType")
    public LotType stringToLotType(String lotType) {
        if (lotType != null && !lotType.isEmpty()) {
            switch (lotType.toLowerCase()) {
                case "products":
                    return LotType.PRODUCT;
                case "services":
                    return LotType.SERVICE;
                case "products and services":
                    return LotType.PRODUCT_AND_SERVICE;
                default:
                    return null;
            }
        }

        return null;
    }

    // Sectors
    @Named("sectorsToStrings")
    public Collection<String> sectorsToStrings(Set<Sector> sectors) {
        Collection<String> model = new ArrayList<>();

        if (sectors != null) {
            model = sectors.stream().map(Sector::getName).collect(Collectors.toList());
        }

        return model;
    }

    // Related Agreement Lots
    @Mapping(source = "agreementSource.number", target = "caNumber")
    @Mapping(source = "lotSource.number", target = "lotNumber")
    @Mapping(source = "sourceRelatedLot.relationship", target = "relationship")
    public abstract RelatedAgreementLot sourcesToRelatedAgreementLot(Lot lotSource, CommercialAgreement agreementSource, LotRelatedLot sourceRelatedLot);

    @Named("lotRelatedLotToRelatedAgreementLots")
    public Collection<RelatedAgreementLot> lotRelatedLotToRelatedAgreementLots(Set<LotRelatedLot> relatedLotSource) {
        if (relatedLotSource != null) {
            return relatedLotSource.stream().map(relatedLot -> {
                Lot lotModel = agreementService.getLot(relatedLot.getRule().getLotId());

                if (lotModel != null) {
                    return sourcesToRelatedAgreementLot(lotModel, lotModel.getAgreement(), relatedLot);
                }

                return null;
            }).collect(Collectors.toSet());
        }

        return null;
    }

    // Rules
    @Mapping(source = "ruleId", target = "ruleId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "service", target = "service")
    @Mapping(source = "lotAttributes", target = "lotAttributes", qualifiedByName = "lotRuleAttributesToNameValueTypes")
    @Mapping(source = "evaluationType", target = "evaluationType", qualifiedByName = "stringToEvaluationType")
    @Mapping(source = "transactionData", target = "transactionData", qualifiedByName = "lotRuleTransactionObjectToTransactionData")
    public abstract LotRuleDTO lotRuleToLotRuleDto(LotRule rule);

    @Named("lotRuleTransactionObjectToTransactionData")
    public abstract TransactionData lotRuleTransactionObjectToTransactionData(LotRuleTransactionObject transModel);

    @Named("lotRuleAttributesToNameValueTypes")
    public abstract Collection<NameValueType> lotRuleAttributesToNameValueTypes(Set<LotRuleAttribute> attributeList);

    @Named("stringToEvaluationType")
    public EvaluationType stringToEvaluationType(String evalType) {
        switch(evalType.toLowerCase()) {
            case "flag":
                return EvaluationType.FLAG;
            case "complex":
                return EvaluationType.COMPLEX;
            case "equal":
                return EvaluationType.EQUAL;
            case "greater":
                return EvaluationType.GREATER;
            case "less":
                return EvaluationType.LESS;
            default:
                return null;
        }
    }

    @Named("lotRulesToLotRulesDto")
    public Collection<LotRuleDTO> lotRulesToLotRulesDto(Set<LotRule> sourceRules) {
        if (sourceRules != null) {
            return sourceRules.stream().map(rule -> {
                return lotRuleToLotRuleDto(rule);
            }).collect(Collectors.toSet());
        }

        return null;
    }

    // Routes to Market
    @Mapping(source = "buyingMethodUrl", target = "buyingSystemUrl")
    @Mapping(target = "maxContractLength", expression = "java(sourcesToContractLength(sourceRoute.getContractLengthMaximumValue(), sourceRoute.getContractLengthUnitOfMeasure()))" )
    @Mapping(target = "minContractLength", expression = "java(sourcesToContractLength(sourceRoute.getContractLengthMinimumValue(), sourceRoute.getContractLengthUnitOfMeasure()))" )
    @Mapping(source = "routeToMarket", target = "buyingMethod", qualifiedByName = "routeToMarketToBuyingMethod")
    public abstract RouteToMarketDTO lotRouteToMarketToRouteToMarketDto(LotRouteToMarket sourceRoute);

    @Named("routeToMarketToBuyingMethod")
    public BuyingMethod routeToMarketToBuyingMethod(RouteToMarket routeToMarket) {
        if (routeToMarket != null) {
            switch (routeToMarket.getName().toUpperCase()) {
                case "FURTHER COMPETITION":
                    return BuyingMethod.FURTHER_COMPETITION;
                case "DIRECT AWARD":
                    return BuyingMethod.DIRECT_AWARD;
                case "MARKETPLACE":
                    return BuyingMethod.MARKETPLACE;
                case "AUCTION":
                    return BuyingMethod.E_AUCTION;
                default:
                    return null;
            }
        }

        return null;
    }

    @Mapping(source = "value", target = "length")
    @Mapping(source = "unitOfMeasure", target = "unit")
    public abstract ContractLength sourcesToContractLength(Short value, String unitOfMeasure);

    @Named("lotRouteToMarketsToRouteToMarketDtos")
    public Collection<RouteToMarketDTO> lotRouteToMarketsToRouteToMarketDtos(Set<LotRouteToMarket> sourceRoutes) {
        if (sourceRoutes != null) {
            return sourceRoutes.stream().map(route -> {
                return lotRouteToMarketToRouteToMarketDto(route);
            }).collect(Collectors.toSet());
        }

        return null;
    }

    // NOTE: It seems that buyerNeeds are not mapped
}
