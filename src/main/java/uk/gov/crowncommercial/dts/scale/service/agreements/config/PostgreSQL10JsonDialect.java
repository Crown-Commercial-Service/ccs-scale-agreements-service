package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import org.hibernate.dialect.PostgreSQL10Dialect;

/**
 * Custom PostgreSQL10 class definition for Hibernate to use
 * 
 * See: https://vladmihalcea.com/hibernate-no-dialect-mapping-for-jdbc-type/
 *
 */
public class PostgreSQL10JsonDialect extends PostgreSQL10Dialect {

  public PostgreSQL10JsonDialect() {
    super();
  }
}
