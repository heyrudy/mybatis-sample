package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.UtilsModule.MutatorOption;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import javax.sql.DataSource;

public interface DataSourceConfigurationModule {

    enum HikariDataSourceBuilder {
        INSTANCE;

        @SafeVarargs
        public static DataSource of(MutatorOption<HikariDataSource>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new HikariDataSource(), (model, option) -> option.apply(model),
                    (a, b) -> a);
        }
    }

    enum HikariConfigBuilder {
        INSTANCE;

        @SafeVarargs
        public static HikariConfig of(MutatorOption<HikariConfig>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new HikariConfig(), (model, option) -> option.apply(model), (a, b) -> a);
        }
    }

    enum DataSourceMutatorOptions {
        INSTANCE;

        public MutatorOption<HikariDataSource> config(HikariConfig config) {
            return MutatorOption.of(
                config,
                (it, v) -> new HikariDataSource(v)
            );
        }
    }

    enum HikariConfigMutatorOptions {
        INSTANCE;

        public MutatorOption<HikariConfig> jdbcUrl(String jdbcUrl) {
            return MutatorOption.of(
                jdbcUrl,
                (it, v) -> {
                    it.setJdbcUrl(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> driverClassName(String driverClassName) {
            return MutatorOption.of(
                driverClassName,
                (it, v) -> {
                    it.setDriverClassName(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> username(String username) {
            return MutatorOption.of(
                username,
                (it, v) -> {
                    it.setUsername(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> password(String password) {
            return MutatorOption.of(
                password,
                (it, v) -> {
                    it.setPassword(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> maximumPoolSize(int maximumPoolSize) {
            return MutatorOption.of(
                maximumPoolSize,
                (it, v) -> {
                    it.setMaximumPoolSize(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> minimumIdle(int minimumIdle) {
            return MutatorOption.of(
                minimumIdle,
                (it, v) -> {
                    it.setMinimumIdle(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> idleTimeout(long idleTimeoutMs) {
            return MutatorOption.of(
                idleTimeoutMs,
                (it, v) -> {
                    it.setIdleTimeout(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> connectionTimeout(long connectionTimeoutMs) {
            return MutatorOption.of(
                connectionTimeoutMs,
                (it, v) -> {
                    it.setConnectionTimeout(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> poolName(String poolName) {
            return MutatorOption.of(
                poolName,
                (it, v) -> {
                    it.setPoolName(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> threadFactory(ThreadFactory threadFactory) {
            return MutatorOption.of(
                threadFactory,
                (it, v) -> {
                    it.setThreadFactory(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> connectionTestQuery(String testQuery) {
            return MutatorOption.of(
                testQuery,
                (it, v) -> {
                    it.setConnectionTestQuery(v);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> dataSourceProperty(String propertyName, String value) {
            return MutatorOption.of2(
                propertyName, value,
                (it, v1, v2) -> {
                    it.addDataSourceProperty(v1, v2);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> autoCommit(boolean autoCommit) {
            return MutatorOption.of(
                autoCommit,
                (it, v) -> {
                    it.setAutoCommit(v);
                    return it;
                }
            );
        }
    }
}
