package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

/**
 * Converts LotOrganisationRole from database into LotSupplier DTO
 */
@Component
public class LotSupplierConverter extends AbstractConverter<LotOrganisationRole, LotSupplier> {

  @Override
  protected LotSupplier convert(final LotOrganisationRole source) {

    final LotSupplier lotSupplier = new LotSupplier();
    final Organisation orgSource = source.getOrganisation();

    // Primary Org Identifier
    // TODO: Dedicated converter?
    final OrganizationIdentifier primaryIdentifier = new OrganizationIdentifier();
    primaryIdentifier.setId(orgSource.getEntityId());
    primaryIdentifier.setLegalName(orgSource.getLegalName());
    primaryIdentifier.setUri(orgSource.getUri());

    // TODO Missing from DB
    primaryIdentifier.setScheme(Scheme.GB_COH);

    // Organisation
    final Organization orgDTO = new Organization();

    orgDTO.setName(orgSource.getLegalName());
    orgDTO.setId(orgSource.getEntityId());
    orgDTO.setIdentifier(primaryIdentifier);

    lotSupplier.setOrganization(orgDTO);

    // TODO: Dedicated converter?
    lotSupplier.setSupplierStatus(SupplierStatus.valueOf(orgSource.getStatus().toUpperCase()));
    return lotSupplier;
  }

}
