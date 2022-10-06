package uk.gov.crowncommercial.dts.scale.service.agreements;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot application entry point.
 *
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
