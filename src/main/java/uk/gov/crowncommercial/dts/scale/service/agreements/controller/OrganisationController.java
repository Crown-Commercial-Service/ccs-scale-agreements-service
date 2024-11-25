package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;

/**
 * Organisation Controller
 */
@RestController
@RequestMapping("/organisation")
@RequiredArgsConstructor
@Slf4j
public class OrganisationController {
    @Autowired
    private BusinessLogicClient businessLogicClient;

    @GetMapping("/identifier/{OrganisationName}")
    public OrganizationIdentifier getOrganisation(@PathVariable(value = "OrganisationName") final String OrganisationName) {
    log.debug("getOrganisation called with values: OrganisationName={}", OrganisationName);

    OrganizationIdentifier model = businessLogicClient.getOrganisation(OrganisationName);

    return model;
  }

  @PatchMapping("/identifier/{OrganisationName}")
  public OrganizationIdentifier partialUpdateOrganisation(@PathVariable(value = "OrganisationName") final String OrganisationName, @RequestBody OrganizationIdentifier organizationIdentifier) {
    log.debug("partialUpdateOrganisation called with values: OrganisationName={}", OrganisationName);

    return businessLogicClient.partialSaveOrganisation(OrganisationName, organizationIdentifier);
  }
}
