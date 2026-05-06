package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidOrganisationException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.OrganisationNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.ContactDetailNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
public class SupplierServiceTest {
    @MockBean
    private EhcacheConfig cacheConfig;

    private static final String AGREEMENT_NUMBER = "RM1000";
    private static final String LOT_NUMBER = "Lot 1";
    private static final String COMPANY_NAME = "A Company Name";
    private static final Scheme ENTITY = Scheme.GBCHC;
    private static final String ID = "123456789";

    @Autowired
    SupplierService supplierService;

    @MockBean
    private OrganisationRepo mockOrganisationRepo;

    @MockBean
    private LotOrganisationRoleRepo mockLotOrganisationRoleRepo;

    @MockBean
    private ContactPointLotOrgRoleRepo mockContactPointLotOrgRoleRepo;

    @MockBean
    private RoleTypeRepo mockRoleTypeRepo;

    @MockBean
    private Organisation mockOrganisation;

    @MockBean
    private Lot mockLot;

    @MockBean
    private ContactDetailRepo mockContactDetailRepo;

    @MockBean
    private ContactPointReasonRepo contactPointReasonRepo;

    private void mockSaveAndFlush() {
        when(mockOrganisationRepo.saveAndFlush(any(Organisation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testGetOrganisation() throws Exception {
        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(mockOrganisation));
        assertEquals(mockOrganisation, supplierService.findOrganisationByLegalName(COMPANY_NAME));
    }

    @Test
    void testGetOrganisationNotFound() throws Exception {
        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(null));

        OrganisationNotFoundException thrown = Assertions.assertThrows(
                OrganisationNotFoundException.class,
                () -> supplierService.findOrganisationByLegalName(COMPANY_NAME),
                "Organisation with legal name of 'A Company Name' not found"
        );

        assertTrue(thrown.getMessage().contains("not found"));
    }

    @Test
    void testGetOrganisationBySchemeAndEntityId() throws Exception {
        when(mockOrganisationRepo.findByRegistryCodeAndEntityId(ENTITY.getName(),ID)).thenReturn(Optional.ofNullable(mockOrganisation));
        assertEquals(mockOrganisation, supplierService.findOrganisationBySchemeAndEntityId(ENTITY.getName(),ID));
    }

    @Test
    void testGetOrganisationBySchemeAndEntityIdNotFound() throws Exception {
        when(mockOrganisationRepo.findByRegistryCodeAndEntityId(ENTITY.getName(),ID)).thenReturn(Optional.ofNullable(null));

        OrganisationNotFoundException thrown = Assertions.assertThrows(
                OrganisationNotFoundException.class,
                () -> supplierService.findOrganisationBySchemeAndEntityId(ENTITY.getName(),ID),
                "Organisation with GB-CHC:123456789 not found"
        );

        assertTrue(thrown.getMessage().contains("not found"));
    }

    @Test
    void testCreateOrganisation() throws Exception {

        Organisation org = new Organisation();
        org.setLegalName(COMPANY_NAME);
        org.setRegistryCode(Scheme.GBCHC.getName());
        org.setEntityId("123456");
        org.setIncorporationDate(LocalDate.now());
        org.setIncorporationCountry("GB");

        when(mockOrganisationRepo.findByEntityIdAndRegistryCode("123456", Scheme.GBCHC.getName())).thenReturn(Optional.empty());
        mockSaveAndFlush();

        Organisation result = supplierService.createOrUpdateOrganisation(org);
        assertEquals(result.getLegalName(), org.getLegalName());
        assertEquals(result.getRegistryCode(), org.getRegistryCode());
        assertEquals(result.getEntityId(), org.getEntityId());
        assertEquals(result.getIncorporationCountry(), org.getIncorporationCountry());
        assertEquals(result.getIncorporationDate(), org.getIncorporationDate());
    }

    Organisation setupOrg(){
        Organisation org = new Organisation();
        org.setId(123);
        org.setLegalName(COMPANY_NAME);
        org.setRegistryCode(Scheme.GBCHC.getName());
        org.setEntityId("123456");
        org.setIncorporationDate(LocalDate.now());
        org.setIncorporationCountry("GB");
        org.setIsActive(true);

        return org;
    }

    ContactDetail setupCd(){
        ContactDetail cd = new ContactDetail();
        cd.setName("Some name");
        cd.setEmailAddress("some@email.com");
        cd.setStreetAddress("Street name");
        cd.setPostalCode("SW1");
        cd.setCountryCode("GB");
        cd.setCountryName("UK");

        return cd;
    }

    ContactPointLotOrgRole setupCplor(){
        ContactPointLotOrgRole cd = new ContactPointLotOrgRole();
        cd.setContactPointName("Some name");

        return cd;
    }

    @Test
    void testUpdateOrganisation() throws Exception {

        Organisation org = setupOrg();

        when(mockOrganisationRepo.findByEntityIdAndRegistryCode(org.getEntityId(), org.getRegistryCode())).thenReturn(Optional.of(org));
        mockSaveAndFlush();

        Organisation result = supplierService.createOrUpdateOrganisation(org);
        assertEquals(result.getLegalName(), org.getLegalName());
        assertEquals(result.getRegistryCode(), org.getRegistryCode());
        assertEquals(result.getEntityId(), org.getEntityId());
        assertEquals(result.getIncorporationCountry(), org.getIncorporationCountry());
        assertEquals(result.getIncorporationDate(), org.getIncorporationDate());
        assertEquals(result.getIsActive(), org.getIsActive());
    }

    private void commonSupplierMocks(Organisation org) {
        when(mockOrganisationRepo.findByEntityIdAndRegistryCode(org.getEntityId(), org.getRegistryCode()))
                .thenReturn(Optional.of(org));

        mockSaveAndFlush();

        RoleType roleType = new RoleType();
        when(mockRoleTypeRepo.findById(2)).thenReturn(roleType);

        ContactPointReason reason = new ContactPointReason();
        when(contactPointReasonRepo.findById(3)).thenReturn(reason);
    }

    @Test
    void testAddSupplierWithoutContact_createRelationship() throws Exception {

        Organisation org = setupOrg();
        commonSupplierMocks(org);

        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(any(Integer.class), any(Integer.class), any(RoleType.class))).thenReturn(Optional.empty());

        supplierService.addSupplierRelationship(mockLot, org, null, "Local Test", SupplierStatus.ACTIVE);

        verify(mockLotOrganisationRoleRepo, times(1)).saveAndFlush(any(LotOrganisationRole.class));
    }

    @Test
    void testAddSupplierWithoutContact_updateRelationship() throws Exception {

        Organisation org = setupOrg();
        commonSupplierMocks(org);

        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(any(Integer.class), any(Integer.class), any(RoleType.class))).thenReturn(Optional.of(new LotOrganisationRole()));

        supplierService.addSupplierRelationship(mockLot, org, null, "Local Test", SupplierStatus.ACTIVE);

        verify(mockLotOrganisationRoleRepo, times(1)).saveAndFlush(any(LotOrganisationRole.class));
    }

    @Test
    void testAddSupplierWithContact_createRelationship() throws Exception {

        Organisation org = setupOrg();
        ContactDetail cd = setupCd();
        LotOrganisationRole lor = new LotOrganisationRole();
        lor.setId(1234);

        commonSupplierMocks(org);

        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(any(Integer.class), any(Integer.class), any(RoleType.class))).thenReturn(Optional.of(lor));

        when(mockContactPointLotOrgRoleRepo.findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(any(Integer.class), any(ContactPointReason.class))).thenReturn(Optional.empty());

        supplierService.addSupplierRelationship(mockLot, org, cd, "Local Test", SupplierStatus.ACTIVE);

        ArgumentCaptor<ContactPointLotOrgRole> captor = ArgumentCaptor.forClass(ContactPointLotOrgRole.class);
        verify(mockContactPointLotOrgRoleRepo, times(1)).saveAndFlush(captor.capture());

        ContactPointLotOrgRole savedRole = captor.getValue();
        assertTrue(savedRole.getPrimary());
    }

    @Test
    void testAddSupplierWithContact_updateRelationship() throws Exception {

        Organisation org = setupOrg();
        ContactDetail cd = setupCd();
        LotOrganisationRole lor = new LotOrganisationRole();
        lor.setId(1234);

        ContactPointLotOrgRole cplor = new ContactPointLotOrgRole();
        cplor.setContactDetail(cd);

        commonSupplierMocks(org);

        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(any(Integer.class), any(Integer.class), any(RoleType.class))).thenReturn(Optional.of(lor));

        when(mockContactPointLotOrgRoleRepo.findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(eq(1234), any(ContactPointReason.class))).thenReturn(Optional.of(cplor));

        supplierService.addSupplierRelationship(mockLot, org, cd, "Local Test", SupplierStatus.ACTIVE);

        ArgumentCaptor<ContactPointLotOrgRole> captor = ArgumentCaptor.forClass(ContactPointLotOrgRole.class);
        verify(mockContactPointLotOrgRoleRepo, times(1)).saveAndFlush(captor.capture());

        ContactPointLotOrgRole savedRole = captor.getValue();
        assertTrue(savedRole.getPrimary());
    }

    @Test
    void testUpdateSupplierWithName() throws Exception {

        Organisation org = new Organisation();
        org.setLegalName(COMPANY_NAME);

        String existingCompanyName = "old Name";

        when(mockOrganisationRepo.findByLegalName(existingCompanyName)).thenReturn(Optional.ofNullable(mockOrganisation));
        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(null));

        String resultCompanyName = supplierService.partialSaveOrganisation(existingCompanyName, org);

        assertNotNull(resultCompanyName);
        assertEquals(COMPANY_NAME, resultCompanyName);
    }

    @Test
    void testUpdateSupplierWithNameButAnotherSupplierHasThatName() throws Exception {

        Organisation org = new Organisation();
        org.setLegalName(COMPANY_NAME);

        String existingCompanyName = "old Name";

        when(mockOrganisationRepo.findByLegalName(existingCompanyName)).thenReturn(Optional.ofNullable(mockOrganisation));
        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(mockOrganisation));

        InvalidOrganisationException thrown = Assertions.assertThrows(
            InvalidOrganisationException.class,
            () -> supplierService.partialSaveOrganisation(existingCompanyName, org),
            "Organisation with legal name:"+COMPANY_NAME+", already exist"
        );

        assertTrue(thrown.getMessage().contains("already exist"));
    }

    @Test
    void testUpdateSupplierWithSchemeAndId() throws Exception {
        
        Organisation org = new Organisation();
        org.setRegistryCode(Scheme.GBCHC.getName());
        org.setEntityId(ID);

        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(setupOrg()));
        when(mockOrganisationRepo.findByRegistryCodeAndEntityId(Scheme.GBCHC.getName(), ID)).thenReturn(Optional.ofNullable(null));

        String resultCompanyName = supplierService.partialSaveOrganisation(COMPANY_NAME, org);
        
        assertNotNull(resultCompanyName);
        assertEquals(COMPANY_NAME, resultCompanyName);

    }

    @Test
    void testUpdateSupplierWithSchemeAndIdButAnotherSupplierHasThatSchemeAndId() throws Exception {

        Organisation org = new Organisation();
        org.setRegistryCode(Scheme.GBCHC.getName());
        org.setEntityId(ID);


        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(mockOrganisation));
        when(mockOrganisationRepo.findByRegistryCodeAndEntityId(Scheme.GBCHC.getName(), ID)).thenReturn(Optional.ofNullable(mockOrganisation));

        InvalidOrganisationException thrown = Assertions.assertThrows(
            InvalidOrganisationException.class,
            () -> supplierService.partialSaveOrganisation(COMPANY_NAME, org),
            "Organisation with scheme:id:"+Scheme.GBCHC.getName() + ":"+ ID+", already exist"
        );

        assertTrue(thrown.getMessage().contains("already exist"));
    }

    @Test
    void testUpdateSupplierAndContactByDuns_success() {
        String dunsNumber = "123456789";
        Organisation org = setupOrg();
        org.setRegistryCode("US-DUNS");
        org.setEntityId(dunsNumber);
        SupplierUpdateRequest updateRequest = new SupplierUpdateRequest();
        updateRequest.setSupplierName("Updated Supplier");
        updateRequest.setEmailAddress("updated@email.com");
        updateRequest.setContactPointName("Updated Name");
        updateRequest.setTelephoneNumber("9876543210");
        updateRequest.setStreetAddress("New St");
        updateRequest.setLocality("New City");
        updateRequest.setPostalCode("NEW123");
        updateRequest.setCountryCode("NP");
        updateRequest.setCountryName("Japan");

        ContactDetail contact = setupCd();
        ContactPointLotOrgRole contactPointLotOrgRole = setupCplor();
        contact.setId(1);
        contactPointLotOrgRole.setId(1);
        java.util.List<ContactDetail> contacts = java.util.Collections.singletonList(contact);
        java.util.List<ContactPointLotOrgRole> contactPointLotOrgRoles = java.util.Collections.singletonList(contactPointLotOrgRole);

        when(mockOrganisationRepo.findByRegistryCodeAndEntityId("US-DUNS", dunsNumber)).thenReturn(java.util.Optional.of(org));
        when(mockContactPointLotOrgRoleRepo.findPrimaryContactDetailsByDuns("US-DUNS", dunsNumber)).thenReturn(contacts);
        when(mockContactPointLotOrgRoleRepo.findPrimaryContactPointLotOrgRoleByDuns("US-DUNS", dunsNumber)).thenReturn(contactPointLotOrgRoles);
        when(mockContactPointLotOrgRoleRepo.save(any(ContactPointLotOrgRole.class))).thenReturn(contactPointLotOrgRole);
        when(mockContactDetailRepo.save(any(ContactDetail.class))).thenReturn(contact);
        when(mockOrganisationRepo.save(any(Organisation.class))).thenReturn(org);

        assertDoesNotThrow(() -> supplierService.updateSupplierAndContactByDuns(dunsNumber, updateRequest));
        verify(mockOrganisationRepo).save(org);
        verify(mockContactDetailRepo).save(contact);
        verify(mockContactPointLotOrgRoleRepo).save(contactPointLotOrgRole);
    }

    @Test
    void testUpdateSupplierAndContactByDuns_organisationNotFound() {
        String dunsNumber = "123456789";
        SupplierUpdateRequest updateRequest = new SupplierUpdateRequest();
        when(mockOrganisationRepo.findByRegistryCodeAndEntityId("US-DUNS", dunsNumber)).thenReturn(java.util.Optional.empty());
        assertThrows(OrganisationNotFoundException.class, () -> supplierService.updateSupplierAndContactByDuns(dunsNumber, updateRequest));
    }

    @Test
    void testUpdateSupplierAndContactByDuns_contactNotFound() {
        String dunsNumber = "123456789";
        Organisation org = setupOrg();
        org.setRegistryCode("US-DUNS");
        org.setEntityId(dunsNumber);
        SupplierUpdateRequest updateRequest = new SupplierUpdateRequest();
        when(mockOrganisationRepo.findByRegistryCodeAndEntityId("US-DUNS", dunsNumber)).thenReturn(java.util.Optional.of(org));
        when(mockContactPointLotOrgRoleRepo.findPrimaryContactDetailsByDuns("US-DUNS", dunsNumber)).thenReturn(java.util.Collections.emptyList());
        assertThrows(ContactDetailNotFoundException.class, () -> supplierService.updateSupplierAndContactByDuns(dunsNumber, updateRequest));
    }

}
