package com.heyrudy.mybatissample.gateway.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDSLContextKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDbSecretPropertiesKey;
import com.heyrudy.mybatissample.domain.spi.config.DependencyKey;
import com.heyrudy.mybatissample.gateway.config.IDbSecretProperties.MockedDbSecretProperties;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.MockedCityRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.sql.DataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.ApplicationContext;

public class SpringAppScopedDependencyLocator implements AppScopedDependencyLocator {

    private final ApplicationContext applicationContext;

    public SpringAppScopedDependencyLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> Either<MissingCriticalDependencyError, T> getDependency(DependencyKey<T> key) {
        return Match(dependencyMap().get(key)).of(
            Case($(key.getType()::isInstance),
                v ->
                    Either.right(key.getType().cast(v))),
            Case($(),
                () ->
                    ErrorMessage.toDependencyError(key)
                        .map(Either::<MissingCriticalDependencyError, T>left)
                        .fold(
                            () -> Either.left(new MissingCriticalDependencyError(
                                "Unknown error for key: %s".formatted(key))),
                            Function.identity()
                        ))
        );
    }

    private Map<DependencyKey<?>, ?> dependencyMap() {
        return createDataSource()
            .fold(
                __ ->
                    Map.ofEntries(Map.entry(
                        CityRepositoryKey.INSTANCE, new MockedCityRepository())),
                dataSource ->
                    Map.ofEntries(
                        Map.entry(
                            CriticalDSLContextKey.INSTANCE,
                            DSL.using(dataSource, SQLDialect.POSTGRES)),
                        Map.entry(
                            CityRepositoryKey.INSTANCE, new MockedCityRepository())
                    )
            );
    }

    private Either<CriticalDbSecretPropertiesNotFoundByDependencyLocatorError, DataSource> createDataSource() {
        return Option.of(getBeanOrMock(MockedDbSecretProperties.class, Option.none()))
            .toEither(new CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(
                AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(CriticalDbSecretPropertiesKey.INSTANCE)))
            .map(iDbSecretProperties ->
                // Create and return the connection pool
                // Using HikariCP -
                // a high-performance JDBC connection pool
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
                    .buildDataSource()
            );
    }

    private <T> T getBeanOrMock(Class<T> beanClass, Option<Supplier<T>> fallback) {
        return applicationContext.getBeanProvider(beanClass)
            .getIfAvailable(() -> fallback.map(Supplier::get).getOrNull());
    }

    public static class HikariConfigBuilder {

        private final HikariConfig config = new HikariConfig();

        private HikariConfigBuilder() {
        }

        public static HikariConfigBuilder create() {
            return new HikariConfigBuilder();
        }

        public HikariConfigBuilder withJdbcUrl(String jdbcUrl) {
            config.setJdbcUrl(jdbcUrl);
            return this;
        }

        public HikariConfigBuilder withUsername(String username) {
            config.setUsername(username);
            return this;
        }

        public HikariConfigBuilder withPassword(String password) {
            config.setPassword(password);
            return this;
        }

        public HikariConfigBuilder withMaximumPoolSize(int maximumPoolSize) {
            config.setMaximumPoolSize(maximumPoolSize);
            return this;
        }

        public HikariConfigBuilder withMinimumIdle(int minimumIdle) {
            config.setMinimumIdle(minimumIdle);
            return this;
        }

        public HikariConfigBuilder withIdleTimeout(long idleTimeoutMs) {
            config.setIdleTimeout(idleTimeoutMs);
            return this;
        }

        public HikariConfigBuilder withConnectionTimeout(long connectionTimeoutMs) {
            config.setConnectionTimeout(connectionTimeoutMs);
            return this;
        }

        public HikariConfigBuilder withPoolName(String poolName) {
            config.setPoolName(poolName);
            return this;
        }

        public HikariConfigBuilder withConnectionTestQuery(String testQuery) {
            config.setConnectionTestQuery(testQuery);
            return this;
        }

        public HikariConfigBuilder withDataSourceProperty(String propertyName, String value) {
            config.addDataSourceProperty(propertyName, value);
            return this;
        }

        public HikariConfigBuilder withAutoCommit(boolean autoCommit) {
            config.setAutoCommit(autoCommit);
            return this;
        }

        public HikariConfig build() {
            return config;
        }

        public DataSource buildDataSource() {
            return new HikariDataSource(config);
        }
    }
}
