package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.MissingCriticalConfigError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

public sealed interface CriticalConfigKey<T>
    extends ConfigKey<T>, DataSourceConfigurationModule, DomainErrorModule
    permits CriticalConfigKey.CriticalPostgresDSLContextConfigKey,
    CriticalConfigKey.CriticalH2DSLContextConfigKey {

    enum CriticalPostgresDSLContextConfigKey
        implements CriticalConfigKey<DSLContext> {
        INSTANCE;

        private static final Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>> CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED =
            CriticalDbSecretKey.INSTANCE.lazyLoad();
        private static final Function<IDbSecret, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DB_SECRET_TO_DSL_CONTEXT =
            iDbSecret ->
                Either.right(
                    DSL.using(new DefaultConfiguration()
                        // Your HikariCP or other DataSource
                        .set(HikariDataSourceBuilder.of(
                            DataSourceMutatorStages.INSTANCE.config(
                                HikariConfigBuilder.of(
                                    // Database connection properties
                                    HikariConfigMutatorStages.INSTANCE.jdbcUrl(
                                        iDbSecret.getJdbcUrl()),
                                    HikariConfigMutatorStages.INSTANCE.username(
                                        iDbSecret.username()),
                                    HikariConfigMutatorStages.INSTANCE.password(
                                        iDbSecret.password()),
                                    // Dynamic connection pool sizing based on available processors
                                    HikariConfigMutatorStages.INSTANCE.maximumPoolSize(
                                        Math.max(
                                            10, Runtime.getRuntime().availableProcessors() * 2)),
                                    HikariConfigMutatorStages.INSTANCE.minimumIdle(
                                        Math.max(
                                            2, Runtime.getRuntime().availableProcessors() / 2)),
                                    HikariConfigMutatorStages.INSTANCE.idleTimeout(20000),
                                    HikariConfigMutatorStages.INSTANCE.connectionTimeout(5000),
                                    HikariConfigMutatorStages.INSTANCE.poolName(
                                        "OptimizedPostgresDbPool"),
                                    // Performance optimization
                                    HikariConfigMutatorStages.INSTANCE.threadFactory(
                                        Thread.ofVirtual()
                                            .name("hikari-virtual-", 0).factory()),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "cachePrepStmts", "true"),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSize", "350"),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSqlLimit", "4096"),
                                    // Optional: connection test query
                                    HikariConfigMutatorStages.INSTANCE.connectionTestQuery(
                                        "SELECT 1"),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "autoReconnect", "true")))))
                        .set(SQLDialect.POSTGRES)
                        .set(Executors.newVirtualThreadPerTaskExecutor())
                    ));
        private static final Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR =
            Either.left(
                new MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError(
                    MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY.formatted(
                        INSTANCE)));
        private static final Function<Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DSL_CONTEXT_TRANSFORMER =
            missingCriticalDependencyErrorIDbSecretPropertiesEither ->
                missingCriticalDependencyErrorIDbSecretPropertiesEither.fold(
                    _ -> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR,
                    DB_SECRET_TO_DSL_CONTEXT);

        @Override
        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> lazyLoad() {
            return CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED
                .map(DSL_CONTEXT_TRANSFORMER);
        }

        @Override
        public String toString() {
            return "CriticalPostgresDSLContextConfigKey{}";
        }
    }

    enum CriticalH2DSLContextConfigKey
        implements CriticalConfigKey<DSLContext> {
        INSTANCE;

        private static final Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>> CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED =
            CriticalDbSecretKey.INSTANCE.lazyLoad();
        private static final Function<IDbSecret, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DB_SECRET_TO_DSL_CONTEXT =
            iDbSecret ->
                Either.right(
                    DSL.using(new DefaultConfiguration()
                        // Your HikariCP or other DataSource
                        .set(HikariDataSourceBuilder.of(
                            DataSourceMutatorStages.INSTANCE.config(
                                HikariConfigBuilder.of(
                                    // Database connection properties
                                    HikariConfigMutatorStages.INSTANCE.driverClassName(
                                        iDbSecret.driverClassName()),
                                    HikariConfigMutatorStages.INSTANCE.jdbcUrl(
                                        iDbSecret.getJdbcUrl()),
                                    HikariConfigMutatorStages.INSTANCE.username(
                                        iDbSecret.username()),
                                    HikariConfigMutatorStages.INSTANCE.password(
                                        iDbSecret.password()),
                                    // Dynamic connection pool sizing based on available processors
                                    HikariConfigMutatorStages.INSTANCE.maximumPoolSize(
                                        Math.max(
                                            10, Runtime.getRuntime().availableProcessors() * 2)),
                                    HikariConfigMutatorStages.INSTANCE.minimumIdle(
                                        Math.max(
                                            2, Runtime.getRuntime().availableProcessors() / 2)),
                                    HikariConfigMutatorStages.INSTANCE.idleTimeout(20000),
                                    HikariConfigMutatorStages.INSTANCE.connectionTimeout(5000),
                                    HikariConfigMutatorStages.INSTANCE.poolName(
                                        "OptimizedH2DbPool"),
                                    // Performance optimization
                                    HikariConfigMutatorStages.INSTANCE.threadFactory(
                                        Thread.ofVirtual()
                                            .name("hikari-virtual-", 0).factory()),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "cachePrepStmts", "true"),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSize", "350"),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSqlLimit", "4096"),
                                    // Optional: connection test query
                                    HikariConfigMutatorStages.INSTANCE.connectionTestQuery(
                                        "SELECT 1"),
                                    HikariConfigMutatorStages.INSTANCE.dataSourceProperty(
                                        "autoReconnect", "true")
                                    // No auto-commit for H2 db engine
                                    //,HikariConfigMutatorStages.INSTANCE.autoCommit(false)
                                ))))
                        .set(SQLDialect.H2)
                        .set(Executors.newVirtualThreadPerTaskExecutor())
                        .set(new Settings().withExecuteLogging(false))));
        private static final Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR =
            Either.left(
                new MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError(
                    MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY.formatted(
                        INSTANCE)));
        private static final Function<Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DSL_CONTEXT_TRANSFORMER =
            missingCriticalDependencyErrorIDbSecretPropertiesEither ->
                missingCriticalDependencyErrorIDbSecretPropertiesEither.fold(
                    _ -> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR,
                    DB_SECRET_TO_DSL_CONTEXT);

        @Override
        public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, DSLContext>> lazyLoad() {
            return CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED.map(DSL_CONTEXT_TRANSFORMER);
        }

        @Override
        public String toString() {
            return "CriticalH2DSLContextConfigKey{}";
        }
    }
}