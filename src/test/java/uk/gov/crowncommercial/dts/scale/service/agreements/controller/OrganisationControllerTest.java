package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rollbar.notifier.Rollbar;

import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationIdentifier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Scheme;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.SupplierService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganisationController.class)
@Import(GlobalErrorHandler.class)
public class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService service;

    @Autowired
    private OrganisationController controller;

    @MockBean
    private BusinessLogicClient businessLogicClient;

    @MockBean
    private Organisation mockOrganisation;

    @MockBean
    private Rollbar rollbar;

    private static final Scheme ENTITY = Scheme.GBCHC;
    private static final String ID = "123456789";
    private static final String LEGAL_NAME = "New Company";

    @Test
    void testGetOrganisationSuccess() throws Exception {
        final OrganizationIdentifier organizationIdentifier = new OrganizationIdentifier();
        organizationIdentifier.setLegalName(LEGAL_NAME);

        when(service.findOrganisationByLegalName(LEGAL_NAME)).thenReturn(mockOrganisation);
        when(controller.getOrganisation(LEGAL_NAME)).thenReturn(organizationIdentifier);
        mockMvc.perform(get("/organisation/identifier/" + LEGAL_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.legalName", is(LEGAL_NAME)));
    }

    @Test
    void testGetOrganisationNotFound() throws Exception {

        when(service.findOrganisationByLegalName(LEGAL_NAME)).thenReturn(null);

        when(controller.getOrganisation(LEGAL_NAME)).thenThrow(new OrganisationNotFoundException(LEGAL_NAME));

        mockMvc.perform(get("/organisation/identifier/" + LEGAL_NAME))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
                .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
                .andExpect(jsonPath("$.errors[0].detail",
                        is(String.format(OrganisationNotFoundException.ERROR_MSG_TEMPLATE, LEGAL_NAME))))
                .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
    }

    @Test
    void testUpdateOrganisationValidName() throws Exception {

        final String newName = "New organisation name";

        OrganizationIdentifier input = new OrganizationIdentifier();
        input.setLegalName(LEGAL_NAME);

        OrganizationIdentifier output = new OrganizationIdentifier();
        output.setLegalName(newName);

        when(controller.partialUpdateOrganisation(LEGAL_NAME, input)).thenReturn(output);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/organisation/identifier/" + LEGAL_NAME)
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.legalName", is(newName)));
    }

    @Test
    void testUpdateOrganisationValidSchemeAndId() throws Exception {

        OrganizationIdentifier input = new OrganizationIdentifier();
        input.setId(ID);
        input.setScheme(ENTITY);

        OrganizationIdentifier output = new OrganizationIdentifier();
        output.setScheme(ENTITY);
        output.setId(ID);

        when(controller.partialUpdateOrganisation(LEGAL_NAME, input)).thenReturn(output);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/organisation/identifier/" + LEGAL_NAME)
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheme", is(ENTITY.getName())))
                .andExpect(jsonPath("$.id", is(ID)));
    }

    @Test
    void testUpdateOrganisationValidSchemeAndIdAndName() throws Exception {

        OrganizationIdentifier input = new OrganizationIdentifier();
        input.setLegalName(LEGAL_NAME);
        input.setId(ID);
        input.setScheme(ENTITY);

        OrganizationIdentifier output = new OrganizationIdentifier();
        output.setLegalName(LEGAL_NAME);
        output.setScheme(ENTITY);
        output.setId(ID);

        when(controller.partialUpdateOrganisation(LEGAL_NAME, input)).thenReturn(output);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/organisation/identifier/" + LEGAL_NAME)
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheme", is(ENTITY.getName())))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.legalName", is(LEGAL_NAME)));
    }

    @Test
    void testUpdateOrganisationValidSchemeOnly() throws Exception {

        OrganizationIdentifier input = new OrganizationIdentifier();
        input.setScheme(ENTITY);

        when(controller.partialUpdateOrganisation(LEGAL_NAME, input)).thenThrow(new InvalidOrganisationException());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/organisation/identifier/" + LEGAL_NAME)
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_VALIDATION_TITLE)))
                .andExpect(jsonPath("$.errors[0].detail",
                        is(String.format(InvalidOrganisationException.ERROR_MSG_TEMPLATE_EMPTY))))
                .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)));
    }

    @Test
    void testUpdateOrganisationInvalidName() throws Exception {

        OrganizationIdentifier input = new OrganizationIdentifier();
        input.setLegalName(LEGAL_NAME);

        when(controller.partialUpdateOrganisation(LEGAL_NAME, input))
                .thenThrow(new InvalidOrganisationException("legal name", LEGAL_NAME));

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/organisation/identifier/" + LEGAL_NAME)
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_VALIDATION_TITLE)))
                .andExpect(jsonPath("$.errors[0].detail",
                        is(String.format(InvalidOrganisationException.ERROR_MSG_TEMPLATE_EXISTED, "legal name",
                                LEGAL_NAME))))
                .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)));
    }

    @Test
    void testUpdateOrganisationInvalidSchemeAndId() throws Exception {

        OrganizationIdentifier input = new OrganizationIdentifier();
        input.setScheme(ENTITY);
        input.setId(ID);

        String errorString = ENTITY.getName() + ":" + ID;
        when(controller.partialUpdateOrganisation(LEGAL_NAME, input))
                .thenThrow(new InvalidOrganisationException("scheme:id", errorString));

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/organisation/identifier/" + LEGAL_NAME)
                .content(asJsonString(input))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.BAD_REQUEST.toString())))
                .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_VALIDATION_TITLE)))
                .andExpect(jsonPath("$.errors[0].detail",
                        is(String.format(InvalidOrganisationException.ERROR_MSG_TEMPLATE_EXISTED, "scheme:id",
                                errorString))))
                .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)));
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

}
