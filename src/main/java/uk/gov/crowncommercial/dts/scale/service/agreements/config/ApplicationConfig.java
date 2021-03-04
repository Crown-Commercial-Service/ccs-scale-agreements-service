package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application Config.
 *
 */
@Configuration
public class ApplicationConfig {

  @Bean
  public ModelMapper modelMapper() {

    // final ModelMapper modelMapper = new ModelMapper();
    // modelMapper.getConfiguration()// .setMatchingStrategy(MatchingStrategies.LOOSE);
    // .setSourceNamingConvention(NamingConventions.NONE)
    // .setDestinationNamingConvention(NamingConventions.NONE);
    // return modelMapper;

    return new ModelMapper();
  }

}
