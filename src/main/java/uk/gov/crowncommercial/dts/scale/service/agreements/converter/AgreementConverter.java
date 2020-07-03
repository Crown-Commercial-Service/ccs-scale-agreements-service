package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import javax.annotation.PostConstruct;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.DataType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EvaluationType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RelatedAgreementLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRelatedLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Encapsulates conversion logic between DTOs and Entities.
 */
@RequiredArgsConstructor
@Component
public class AgreementConverter {

  @Autowired
  private ModelMapper modelMapper;

  private final AgreementService service;

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

    Converter<String, EvaluationType> stringToEvaluationTypeConverter =
        new AbstractConverter<String, EvaluationType>() {
          @Override
          protected EvaluationType convert(String type) {
            if ("flag".equalsIgnoreCase(type)) {
              return EvaluationType.FLAG;
            } else if ("complex".equalsIgnoreCase(type)) {
              return EvaluationType.COMPLEX;
            } else if ("equal".equalsIgnoreCase(type)) {
              return EvaluationType.EQUAL;
            } else if ("greater".equalsIgnoreCase(type)) {
              return EvaluationType.GREATER;
            } else if ("less".equalsIgnoreCase(type)) {
              return EvaluationType.LESS;
            }
            return null;
          }
        };

    Converter<String, DataType> stringToDataTypeConverter =
        new AbstractConverter<String, DataType>() {
          @Override
          protected DataType convert(String type) {
            if ("integer".equalsIgnoreCase(type)) {
              return DataType.INTEGER;
            } else if ("number".equalsIgnoreCase(type)) {
              return DataType.NUMBER;
            } else if ("string".equalsIgnoreCase(type)) {
              return DataType.STRING;
            } else if ("date".equalsIgnoreCase(type)) {
              return DataType.DATE;
            }
            return null;
          }
        };


    Converter<LotRelatedLot, RelatedAgreementLot> relatedLotConverter =
        new AbstractConverter<LotRelatedLot, RelatedAgreementLot>() {
          @Override
          protected RelatedAgreementLot convert(LotRelatedLot source) {
            /*
             * Introduced this explicit call as a workaround to automated bidirectional mapping
             * issues (infinite loop issues). Couldn't get a more elegant solution to work in the
             * time frame.
             */
            Lot lot = service.getLot(source.getRule().getLotId());
            RelatedAgreementLot ral = new RelatedAgreementLot();
            ral.setCaNumber(lot.getAgreement().getNumber());
            ral.setLotNumber(lot.getNumber());
            ral.setRelationship(source.getRelationship());
            return ral;
          }
        };

    modelMapper.addConverter(sectorToStringConverter);
    modelMapper.addConverter(stringToLotTypeConverter);
    modelMapper.addConverter(stringToEvaluationTypeConverter);
    modelMapper.addConverter(stringToDataTypeConverter);
    modelMapper.addConverter(relatedLotConverter);
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
