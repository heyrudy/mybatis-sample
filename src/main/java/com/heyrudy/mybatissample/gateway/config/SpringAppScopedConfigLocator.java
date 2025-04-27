package com.heyrudy.mybatissample.gateway.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDataSourceNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundBySecretLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.ConfigKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalConfigKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDSLContextKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDataSourceKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDbSecretPropertiesKey;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class SpringAppScopedConfigLocator implements AppScopedConfigLocator {

    private final AppScopedSecretLocator appScopedSecretLocator;

    public SpringAppScopedConfigLocator(AppScopedSecretLocator appScopedSecretLocator) {
        this.appScopedSecretLocator = appScopedSecretLocator;
    }

    @Override
    public <T> Either<CriticalDataSourceNotFoundByConfigLocatorError, T> getCriticalDataSourceConfig(
        CriticalConfigKey<T> key) {
        return getConfig(key)
            .toEither(new CriticalDataSourceNotFoundByConfigLocatorError(
                ErrorMessage.NO_CRITICAL_DATA_SOURCE_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(key)));
    }

    @Override
    public <T> Either<CriticalDSLContextNotFoundByConfigLocatorError, T> getCriticalDSLContextConfig(
        CriticalConfigKey<T> key) {
        return getConfig(key)
            .toEither(new CriticalDSLContextNotFoundByConfigLocatorError(
                ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(key)));
    }

    @Override
    public <T> Option<T> getConfig(ConfigKey<T> key) {
        return Option.of(configMap().get(key))
            .filter(key.getType()::isInstance)
            .flatMap(it ->
                Match(it).option(
                    Case($(key.getType()::isInstance), key.getType()::cast)));
    }

    @Override
    public boolean hasConfig(ConfigKey<?> key) {
        return configMap().containsKey(key);
    }

    private Map<ConfigKey<?>, ?> configMap() {
        return Stream.of(dbConfigMap())
            .flatMap(it -> it.entrySet().stream())
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<ConfigKey<?>, ?> dbConfigMap() {
        return createDataSource()
            .fold(
                criticalDbSecretPropertiesNotFoundBySecretLocatorError ->
                    Map.of(),
                dataSource ->
                    Map.ofEntries(
                        Map.entry(
                            CriticalDataSourceKey.INSTANCE,
                            dataSource),
                        Map.entry(
                            CriticalDSLContextKey.INSTANCE,
                            DSL.using(dataSource, SQLDialect.POSTGRES))
                    )
            );
    }

    // Method to create a DataSource for jOOQ
    private Either<CriticalDbSecretPropertiesNotFoundBySecretLocatorError, DataSource> createDataSource() {
        return appScopedSecretLocator.getCriticalDbSecretProperties(
                CriticalDbSecretPropertiesKey.INSTANCE)
            .bimap(
                Function.identity(),
                iDbSecretProperties ->
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
