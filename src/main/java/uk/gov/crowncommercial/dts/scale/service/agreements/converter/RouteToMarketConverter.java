package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.BuyingMethod;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ContractLength;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RouteToMarketDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RouteToMarket;

/**
 * Converts LotRouteToMarket from database into RouteToMarketDTO.
 *
 */
@Component
public class RouteToMarketConverter extends AbstractConverter<LotRouteToMarket, RouteToMarketDTO> {

  @Override
  protected RouteToMarketDTO convert(LotRouteToMarket lrtm) {
    RouteToMarketDTO dto = new RouteToMarketDTO();
    dto.setBuyingSystemUrl(lrtm.getBuyingMethodUrl());
    dto.setLocation(lrtm.getLocation());
    dto.setMaxContractLength(getContractLength(lrtm.getContractLengthMaximumValue(),
        lrtm.getContractLengthUnitOfMeasure()));
    dto.setMaximumValue(lrtm.getMaximumValue());
    dto.setMinContractLength(getContractLength(lrtm.getContractLengthMinimumValue(),
        lrtm.getContractLengthUnitOfMeasure()));
    dto.setMinimumValue(lrtm.getMinimumValue());
    dto.setBuyingMethod(getBuyingMethod(lrtm.getRouteToMarket()));
    return dto;
  }

  private ContractLength getContractLength(Short value, String oum) {
    if (value == null && oum == null) {
      return null;
    }
    return new ContractLength(oum, value);
  }

  private BuyingMethod getBuyingMethod(RouteToMarket rtm) {
    switch (rtm.getName().toUpperCase()) {
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
}
