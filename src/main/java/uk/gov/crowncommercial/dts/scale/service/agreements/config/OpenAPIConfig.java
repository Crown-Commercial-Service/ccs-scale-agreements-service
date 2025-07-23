package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Agreements Service API")
                        .version("v1")
                        .description("This is the documentation for the Agreements Service.")
                        .contact(new Contact().name("Support Team").email("support@example.com")));
    }
}
