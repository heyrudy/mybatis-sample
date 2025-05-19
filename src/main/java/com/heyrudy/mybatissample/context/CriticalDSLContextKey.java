package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

public enum CriticalDSLContextKey
    implements CriticalConfigKey<DSLContext> {
    INSTANCE;

    private static final CriticalDbSecretPropertiesKey CRITICAL_DB_SECRET_PROPERTIES_KEY = CriticalDbSecretPropertiesKey.INSTANCE;
    private static final Function<IDbSecretProperties, Either<? extends MissingCriticalDependencyError, DSLContext>> DB_SECRET_PROPERTIES_TO_DSL_CONTEXT =
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
                .set(Executors.newVirtualThreadPerTaskExecutor())));
    private static final Either<CriticalDSLContextNotFoundByDependencyLocatorError, DSLContext> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        Either.left(new CriticalDSLContextNotFoundByDependencyLocatorError(
            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE
                .formatted(INSTANCE)));
    private static final Function<Either<? extends MissingCriticalDependencyError, IDbSecretProperties>, Either<? extends MissingCriticalDependencyError, DSLContext>> DSL_CONTEXT_TRANSFORMER_PATH =
        missingCriticalDependencyErrorIDbSecretPropertiesEither ->
            missingCriticalDependencyErrorIDbSecretPropertiesEither.fold(
                __ -> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH,
                DB_SECRET_PROPERTIES_TO_DSL_CONTEXT);

    @Override
    public Reader<AppScopedDependencyLocator, Either<? extends MissingCriticalDependencyError, DSLContext>> describeDependencyContext() {
        return CRITICAL_DB_SECRET_PROPERTIES_KEY.describeDependencyContext()
            .map(DSL_CONTEXT_TRANSFORMER_PATH);
    }

    @Override
    public String toString() {
        return "CriticalDSLContextKey{}";
    }
}