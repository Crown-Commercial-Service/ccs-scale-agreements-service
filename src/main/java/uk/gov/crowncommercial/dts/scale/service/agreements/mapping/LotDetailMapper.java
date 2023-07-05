package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MapStruct mapping definition for converting Lot objects to and from LotDetail objects
 */
@Mapper(componentModel = "spring")
public interface LotDetailMapper {
    LotDetailMapper INSTANCE = Mappers.getMapper(LotDetailMapper.class);

    @Mapping(source = "lotType", target = "type", qualifiedByName = "stringToLotType")
    @Mapping(source = "sectors", target = "sectors", qualifiedByName = "sectorsToStrings")
    LotDetail lotToLotDetail(Lot dbModel);

    // Mapping of child entities starts here
    // Type
    @Named("stringToLotType")
    public static LotType stringToLotType(String lotType) {
        if ("Products".equalsIgnoreCase(lotType)) {
            return LotType.PRODUCT;
        } else if ("Services".equalsIgnoreCase(lotType)) {
            return LotType.SERVICE;
        } else if ("Products and Services".equalsIgnoreCase(lotType)) {
            return LotType.PRODUCT_AND_SERVICE;
        }
        return null;
    }

    // Sectors
    @Named("sectorsToStrings")
    public static Collection<String> sectorsToStrings(Set<Sector> sectors) {
        Collection<String> model = new ArrayList<>();

        if (sectors != null) {
            model = sectors.stream().map(Sector::getName).collect(Collectors.toList());
        }

        return model;
    }

    // TODO
    // Routes to Market
    // relatedAgreementLots
    // buyerNeeds
    // rules

}
