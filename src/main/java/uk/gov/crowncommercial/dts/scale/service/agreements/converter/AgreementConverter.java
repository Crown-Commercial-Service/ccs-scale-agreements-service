package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import javax.annotation.PostConstruct;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;

/**
 * Encapsulates conversion logic between DTOs and Entities.
 */
@Component
public class AgreementConverter {

  @Autowired
  private ModelMapper modelMapper;

  @PostConstruct
  public void init() {
    Converter<Sector, String> sectorToStringConverter = new AbstractConverter<Sector, String>() {
      @Override
      protected String convert(Sector source) {
        return source == null ? null : source.getName();
      }
    };

    Converter<String, LotType> stringToLotTypeConverter = new AbstractConverter<String, LotType>() {
      @Override
      protected LotType convert(String type) {

        // Temporary code until Trevor aligns data enums with Dave's API enums
        if ("Products".equalsIgnoreCase(type)) {
          return LotType.PRODUCT;
        } else if ("Services".equalsIgnoreCase(type)) {
          return LotType.SERVICE;
        } else if ("Products and Services".equalsIgnoreCase(type)) {
          return LotType.PRODUCT_AND_SERVICE;
        }

        return null;
      }
    };

    modelMapper.addConverter(sectorToStringConverter);
    modelMapper.addConverter(stringToLotTypeConverter);
  }

  public AgreementDetail convertAgreementToDTO(CommercialAgreement ca) {
    return modelMapper.map(ca, AgreementDetail.class);
  }

  public AgreementSummary convertAgreementToSummaryDTO(CommercialAgreement ca) {
    return modelMapper.map(ca, AgreementSummary.class);
  }

  public LotDetail convertLotToDTO(Lot lot) {
    return modelMapper.map(lot, LotDetail.class);
  }

}
