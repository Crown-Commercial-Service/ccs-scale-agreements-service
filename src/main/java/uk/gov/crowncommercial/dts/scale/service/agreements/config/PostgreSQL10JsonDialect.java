package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQL10Dialect;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

/**
 * This class gets around the error `javax.persistence.PersistenceException:
 * org.hibernate.MappingException: No Dialect mapping for JDBC type: 1111` caused by the custom
 * JSONB TypeDef in {@link ProcurementQuestionTemplate}
 * 
 * See: https://vladmihalcea.com/hibernate-no-dialect-mapping-for-jdbc-type/
 *
 */
public class PostgreSQL10JsonDialect extends PostgreSQL10Dialect {

  public PostgreSQL10JsonDialect() {
    super();
  }
}
