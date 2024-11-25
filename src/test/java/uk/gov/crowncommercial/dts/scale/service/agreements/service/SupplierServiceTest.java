package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidOrganisationException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.OrganisationNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
@ExtendWith(MockitoExtension.class)
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

        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(null)).thenReturn(Optional.ofNullable(org));

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

    @Test
    void testUpdateOrganisation() throws Exception {

        Organisation org = setupOrg();

        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(org)).thenReturn(Optional.ofNullable(org));

        Organisation result = supplierService.createOrUpdateOrganisation(org);
        assertEquals(result.getLegalName(), org.getLegalName());
        assertEquals(result.getRegistryCode(), org.getRegistryCode());
        assertEquals(result.getEntityId(), org.getEntityId());
        assertEquals(result.getIncorporationCountry(), org.getIncorporationCountry());
        assertEquals(result.getIncorporationDate(), org.getIncorporationDate());
        assertEquals(result.getIsActive(), org.getIsActive());
    }

    @Test
    void testAddSupplierWithoutContact_createRelationship() throws Exception {

        Organisation org = setupOrg();

        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(org)).thenReturn(Optional.ofNullable(org));
        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(mockLot.getId(),org.getId(), null)).thenReturn(Optional.ofNullable(null));

        supplierService.addSupplierRelationship(mockLot, org, null, "Local Test", SupplierStatus.ACTIVE);

        verify(mockLotOrganisationRoleRepo, times(1)).saveAndFlush(ArgumentMatchers.any(LotOrganisationRole.class));
    }

    @Test
    void testAddSupplierWithoutContact_updateRelationship() throws Exception {

        Organisation org = setupOrg();

        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(org)).thenReturn(Optional.ofNullable(org));
        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(mockLot.getId(),org.getId(), null)).thenReturn(Optional.ofNullable(new LotOrganisationRole()));

        supplierService.addSupplierRelationship(mockLot, org, null, "Local Test", SupplierStatus.ACTIVE);

        verify(mockLotOrganisationRoleRepo, times(1)).saveAndFlush(ArgumentMatchers.any(LotOrganisationRole.class));
    }

    @Test
    void testAddSupplierWithContact_createRelationship() throws Exception {

        Organisation org = setupOrg();
        ContactDetail cd = setupCd();
        LotOrganisationRole lor = new LotOrganisationRole();
        lor.setId(1234);

        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(org)).thenReturn(Optional.ofNullable(org));
        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(mockLot.getId(),org.getId(), null)).thenReturn(Optional.ofNullable(lor));
        when(mockContactPointLotOrgRoleRepo.findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(ContactPointReason.class))).thenReturn(null);

        supplierService.addSupplierRelationship(mockLot, org, cd, "Local Test", SupplierStatus.ACTIVE);

        verify(mockContactPointLotOrgRoleRepo, times(1)).saveAndFlush(ArgumentMatchers.any(ContactPointLotOrgRole.class));
    }

    @Test
    void testAddSupplierWithContact_updateRelationship() throws Exception {

        Organisation org = setupOrg();
        ContactDetail cd = setupCd();
        LotOrganisationRole lor = new LotOrganisationRole();
        lor.setId(1234);

        ContactPointLotOrgRole cplor = new ContactPointLotOrgRole();
        cplor.setContactDetail(cd);


        when(mockOrganisationRepo.findByLegalName(COMPANY_NAME)).thenReturn(Optional.ofNullable(org)).thenReturn(Optional.ofNullable(org));
        when(mockLotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(mockLot.getId(), org.getId(), null)).thenReturn(Optional.ofNullable(lor));
        when(mockContactPointLotOrgRoleRepo.findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(1234, null)).thenReturn(Optional.of(cplor));


        supplierService.addSupplierRelationship(mockLot, org, cd, "Local Test", SupplierStatus.ACTIVE);

        verify(mockContactPointLotOrgRoleRepo, times(1)).saveAndFlush(ArgumentMatchers.any(ContactPointLotOrgRole.class));
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

}
