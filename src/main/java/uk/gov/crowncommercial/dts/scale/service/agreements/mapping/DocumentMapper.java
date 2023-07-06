package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.mapstruct.Mapper;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * MapStruct mapping definition for converting CommercialAgreementDocument objects to and from Document objects
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper {
    Document commercialAgreementDocumentToDocument(CommercialAgreementDocument dbModel);

    default Instant map(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant();
    }
}
