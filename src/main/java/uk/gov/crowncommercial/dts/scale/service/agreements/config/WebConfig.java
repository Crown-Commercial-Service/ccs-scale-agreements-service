package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.dto.BuyingMethodConverter;

/**
 * Spring web / MVC related config
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final BuyingMethodConverter buyingMethodConverter;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(buyingMethodConverter);
  }

}
