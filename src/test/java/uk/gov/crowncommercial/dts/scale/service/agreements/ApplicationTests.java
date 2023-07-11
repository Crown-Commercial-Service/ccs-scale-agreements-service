package uk.gov.crowncommercial.dts.scale.service.agreements;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.controller.AgreementController;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
class ApplicationTests {
    @MockBean
    private EhcacheConfig cacheConfig;

    @Autowired
    private AgreementController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}