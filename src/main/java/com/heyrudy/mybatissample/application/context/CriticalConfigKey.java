package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.HikariConfigBuilder.HikariConfigMutatorOptions;
import com.heyrudy.mybatissample.application.context.HikariDataSourceBuilder.DataSourceMutatorOptions;
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
    extends ConfigKey<T>, DomainErrorModule
    permits CriticalConfigKey.CriticalPostgresDSLContextConfigKey,
    CriticalConfigKey.CriticalH2DSLContextConfigKey {

    enum CriticalPostgresDSLContextConfigKey
        implements CriticalConfigKey<DSLContext> {
        INSTANCE;

        private static final Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>> CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED_PATH =
            CriticalDbSecretKey.INSTANCE.lazyLoad();
        private static final Function<IDbSecret, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DB_SECRET_TO_DSL_CONTEXT =
            iDbSecret ->
                Either.right(
                    DSL.using(new DefaultConfiguration()
                        // Your HikariCP or other DataSource
                        .set(HikariDataSourceBuilder.of(
                            DataSourceMutatorOptions.INSTANCE.config(
                                HikariConfigBuilder.of(
                                    // Database connection properties
                                    HikariConfigMutatorOptions.INSTANCE.jdbcUrl(
                                        iDbSecret.getJdbcUrl()),
                                    HikariConfigMutatorOptions.INSTANCE.username(
                                        iDbSecret.username()),
                                    HikariConfigMutatorOptions.INSTANCE.password(
                                        iDbSecret.password()),
                                    // Dynamic connection pool sizing based on available processors
                                    HikariConfigMutatorOptions.INSTANCE.maximumPoolSize(
                                        Math.max(
                                            10, Runtime.getRuntime().availableProcessors() * 2)),
                                    HikariConfigMutatorOptions.INSTANCE.minimumIdle(
                                        Math.max(
                                            2, Runtime.getRuntime().availableProcessors() / 2)),
                                    HikariConfigMutatorOptions.INSTANCE.idleTimeout(20000),
                                    HikariConfigMutatorOptions.INSTANCE.connectionTimeout(5000),
                                    HikariConfigMutatorOptions.INSTANCE.poolName(
                                        "OptimizedPostgresDbPool"),
                                    // Performance optimization
                                    HikariConfigMutatorOptions.INSTANCE.threadFactory(
                                        Thread.ofVirtual()
                                            .name("hikari-virtual-", 0).factory()),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "cachePrepStmts", "true"),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSize", "350"),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSqlLimit", "4096"),
                                    // Optional: connection test query
                                    HikariConfigMutatorOptions.INSTANCE.connectionTestQuery(
                                        "SELECT 1"),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "autoReconnect", "true")))))
                        .set(SQLDialect.POSTGRES)
                        .set(Executors.newVirtualThreadPerTaskExecutor())
                    ));
        private static final Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
            Either.left(
                new MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError(
                    MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY.formatted(
                        INSTANCE)));
        private static final Function<Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DSL_CONTEXT_TRANSFORMER_PATH =
            missingCriticalDependencyErrorIDbSecretPropertiesEither ->
                missingCriticalDependencyErrorIDbSecretPropertiesEither.fold(
                    __ -> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH,
                    DB_SECRET_TO_DSL_CONTEXT);

        @Override
        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> lazyLoad() {
            return CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED_PATH
                .map(DSL_CONTEXT_TRANSFORMER_PATH);
        }

        @Override
        public String toString() {
            return "CriticalPostgresDSLContextConfigKey{}";
        }
    }

    enum CriticalH2DSLContextConfigKey
        implements CriticalConfigKey<DSLContext> {
        INSTANCE;

        private static final Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>> CRITICAL_DB_SECRET_DEPENDENCY_LAZY_LOADED_PATH =
            CriticalDbSecretKey.INSTANCE.lazyLoad();
        private static final Function<IDbSecret, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DB_SECRET_TO_DSL_CONTEXT =
            iDbSecret ->
                Either.right(
                    DSL.using(new DefaultConfiguration()
                        // Your HikariCP or other DataSource
                        .set(HikariDataSourceBuilder.of(
                            DataSourceMutatorOptions.INSTANCE.config(
                                HikariConfigBuilder.of(
                                    // Database connection properties
                                    HikariConfigMutatorOptions.INSTANCE.driverClassName(
                                        iDbSecret.driverClassName()),
                                    HikariConfigMutatorOptions.INSTANCE.jdbcUrl(
                                        iDbSecret.getJdbcUrl()),
                                    HikariConfigMutatorOptions.INSTANCE.username(
                                        iDbSecret.username()),
                                    HikariConfigMutatorOptions.INSTANCE.password(
                                        iDbSecret.password()),
                                    // Dynamic connection pool sizing based on available processors
                                    HikariConfigMutatorOptions.INSTANCE.maximumPoolSize(
                                        Math.max(
                                            10, Runtime.getRuntime().availableProcessors() * 2)),
                                    HikariConfigMutatorOptions.INSTANCE.minimumIdle(
                                        Math.max(
                                            2, Runtime.getRuntime().availableProcessors() / 2)),
                                    HikariConfigMutatorOptions.INSTANCE.idleTimeout(20000),
                                    HikariConfigMutatorOptions.INSTANCE.connectionTimeout(5000),
                                    HikariConfigMutatorOptions.INSTANCE.poolName(
                                        "OptimizedH2DbPool"),
                                    // Performance optimization
                                    HikariConfigMutatorOptions.INSTANCE.threadFactory(
                                        Thread.ofVirtual()
                                            .name("hikari-virtual-", 0).factory()),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "cachePrepStmts", "true"),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSize", "350"),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "prepStmtCacheSqlLimit", "4096"),
                                    // Optional: connection test query
                                    HikariConfigMutatorOptions.INSTANCE.connectionTestQuery(
                                        "SELECT 1"),
                                    HikariConfigMutatorOptions.INSTANCE.dataSourceProperty(
                                        "autoReconnect", "true")
                                    // No auto-commit for H2 db engine
                                    //,HikariConfigMutatorOptions.INSTANCE.autoCommit(false)
                                ))))
                        .set(SQLDialect.H2)
                        .set(Executors.newVirtualThreadPerTaskExecutor())
                        .set(new Settings().withExecuteLogging(false))));
        private static final Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
            Either.left(
                new MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError(
                    MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY.formatted(
                        INSTANCE)));
        private static final Function<Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> DSL_CONTEXT_TRANSFORMER_PATH =
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
            return "CriticalH2DSLContextConfigKey{}";
        }
    }
}