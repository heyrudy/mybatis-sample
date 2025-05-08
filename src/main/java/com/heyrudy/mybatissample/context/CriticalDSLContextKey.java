package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.Arrays;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public enum CriticalDSLContextKey
    implements CriticalConfigKey<DSLContext> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<? extends MissingCriticalDependencyError, DSLContext>> describeDependencyContext() {
        return CriticalDbSecretPropertiesKey.INSTANCE.describeDependencyContext()
            .map(missingCriticalDependencyErrorIDbSecretPropertiesEither ->
                missingCriticalDependencyErrorIDbSecretPropertiesEither
                    .mapLeft(__ ->
                        new CriticalDSLContextNotFoundByDependencyLocatorError(
                            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE
                                .formatted(INSTANCE)))
                    .map(iDbSecretProperties ->
                        DSL.using(
                            HikariConfigBuilder.create()
                                // Database connection properties
                                .withJdbcUrl(iDbSecretProperties.getJdbcUrl())
                                .withUsername(iDbSecretProperties.username())
                                .withPassword(Arrays.toString(iDbSecretProperties.password()))
                                // Connection pool settings
                                .withMaximumPoolSize(10)
                                .withMinimumIdle(2)
                                .withIdleTimeout(30000)
                                .withConnectionTimeout(10000)
                                .withPoolName("CityDbPool")
                                // Optional: connection test query
                                .withConnectionTestQuery("SELECT 1")
                                // Performance optimization
                                .withDataSourceProperty("cachePrepStmts", "true")
                                .withDataSourceProperty("prepStmtCacheSize", "250")
                                .withDataSourceProperty("prepStmtCacheSqlLimit", "2048")
                                .buildDataSource(),
                            SQLDialect.POSTGRES)));
    }

    @Override
    public String toString() {
        return "CriticalDSLContextKey{}";
    }
}