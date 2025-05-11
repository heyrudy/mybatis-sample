package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.Arrays;
import java.util.concurrent.Executors;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

public enum CriticalDSLContextKey
    implements CriticalConfigKey<DSLContext> {
    INSTANCE;

    private static final CriticalDbSecretPropertiesKey CRITICAL_DB_SECRET_PROPERTIES_KEY = CriticalDbSecretPropertiesKey.INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<? extends MissingCriticalDependencyError, DSLContext>> describeDependencyContext() {
        return CRITICAL_DB_SECRET_PROPERTIES_KEY.describeDependencyContext()
            .map(missingCriticalDependencyErrorIDbSecretPropertiesEither ->
                missingCriticalDependencyErrorIDbSecretPropertiesEither.fold(
                    __ ->
                        Either.left(new CriticalDSLContextNotFoundByDependencyLocatorError(
                            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE
                                .formatted(INSTANCE))),
                    iDbSecretProperties ->
                        Either.right(DSL.using(new DefaultConfiguration()
                            .set(HikariConfigBuilder.create()
                                // Database connection properties
                                .withJdbcUrl(iDbSecretProperties.getJdbcUrl())
                                .withUsername(iDbSecretProperties.username())
                                .withPassword(Arrays.toString(iDbSecretProperties.password()))
                                // Dynamic connection pool sizing based on available processors
                                .withMaximumPoolSize(
                                    Math.max(10, Runtime.getRuntime().availableProcessors() * 2))
                                .withMinimumIdle(
                                    Math.max(2, Runtime.getRuntime().availableProcessors() / 2))
                                .withIdleTimeout(20000)
                                .withConnectionTimeout(5000)
                                .withPoolName("OptimizedDbPool")
                                // Performance optimization
                                .withThreadFactory(Thread.ofVirtual()
                                    .name("hikari-virtual-", 0)
                                    .factory())
                                .withDataSourceProperty("cachePrepStmts", "true")
                                .withDataSourceProperty("prepStmtCacheSize", "350")
                                .withDataSourceProperty("prepStmtCacheSqlLimit", "4096")
                                // Optional: connection test query
                                .withConnectionTestQuery("SELECT 1")
                                .withDataSourceProperty("autoReconnect", "true")
                                .buildDataSource())  // Your HikariCP or other DataSource
                            .set(SQLDialect.POSTGRES)
                            .set(Executors.newVirtualThreadPerTaskExecutor())))));
    }

    @Override
    public String toString() {
        return "CriticalDSLContextKey{}";
    }
}