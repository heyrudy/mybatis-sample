package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.error.CriticalDSLContextNotFoundByDependencyLocatorError.ErrorMessage;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

public enum CriticalPostgresDSLContextConfigKey
    implements CriticalConfigKey<DSLContext> {
    INSTANCE;

    private static final Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IDbSecret>> CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED_PATH =
        CriticalDbSecretKey.INSTANCE.lazyLoad();
    private static final Function<IDbSecret, Either<MissingCriticalDependencyError, DSLContext>> DB_SECRET_TO_DSL_CONTEXT =
        iDbSecret ->
            Either.right(DSL.using(new DefaultConfiguration()
                .set(HikariConfigBuilder.create()
                    // Database connection properties
                    .withJdbcUrl(iDbSecret.getJdbcUrl())
                    .withUsername(iDbSecret.username())
                    .withPassword(iDbSecret.password())
                    // Dynamic connection pool sizing based on available processors
                    .withMaximumPoolSize(
                        Math.max(10, Runtime.getRuntime().availableProcessors() * 2))
                    .withMinimumIdle(
                        Math.max(2, Runtime.getRuntime().availableProcessors() / 2))
                    .withIdleTimeout(20000)
                    .withConnectionTimeout(5000)
                    .withPoolName("OptimizedPostgresDbPool")
                    // Performance optimization
                    .withThreadFactory(
                        Thread.ofVirtual()
                            .name("hikari-virtual-", 0).factory())
                    .withDataSourceProperty("cachePrepStmts", "true")
                    .withDataSourceProperty("prepStmtCacheSize", "350")
                    .withDataSourceProperty("prepStmtCacheSqlLimit", "4096")
                    // Optional: connection test query
                    .withConnectionTestQuery("SELECT 1")
                    .withDataSourceProperty("autoReconnect", "true")
                    .buildDataSource())  // Your HikariCP or other DataSource
                .set(SQLDialect.POSTGRES)
                .set(Executors.newVirtualThreadPerTaskExecutor())));
    private static final Either<MissingCriticalDependencyError, DSLContext> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        Either.left(new CriticalDSLContextNotFoundByDependencyLocatorError(
            ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY.formatted(INSTANCE)));
    private static final Function<Either<MissingCriticalDependencyError, IDbSecret>, Either<MissingCriticalDependencyError, DSLContext>> DSL_CONTEXT_TRANSFORMER_PATH =
        missingCriticalDependencyErrorIDbSecretPropertiesEither ->
            missingCriticalDependencyErrorIDbSecretPropertiesEither.fold(
                __ -> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH,
                DB_SECRET_TO_DSL_CONTEXT);

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, DSLContext>> lazyLoad() {
        return CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED_PATH
            .map(DSL_CONTEXT_TRANSFORMER_PATH);
    }

    @Override
    public String toString() {
        return "CriticalPostgresDSLContextConfigKey{}";
    }
}