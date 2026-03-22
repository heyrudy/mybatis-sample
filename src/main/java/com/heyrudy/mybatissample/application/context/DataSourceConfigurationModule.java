package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.UtilsModule.MutatorStage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Stream;
import javax.sql.DataSource;

public interface DataSourceConfigurationModule {

    enum HikariDataSourceBuilder {
        INSTANCE;

        @SafeVarargs
        public static DataSource of(final MutatorStage<HikariDataSource>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    new HikariDataSource(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }
    }

    enum HikariConfigBuilder {
        INSTANCE;

        @SafeVarargs
        public static HikariConfig of(final MutatorStage<HikariConfig>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    new HikariConfig(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }
    }

    enum DataSourceMutatorStages {
        INSTANCE;

        public MutatorStage<HikariDataSource> config(final HikariConfig config) {
            return MutatorStage.of(
                config,
                (it, v) -> new HikariDataSource(v)
            );
        }
    }

    enum HikariConfigMutatorStages {
        INSTANCE;

        public MutatorStage<HikariConfig> jdbcUrl(final String jdbcUrl) {
            return MutatorStage.of(
                jdbcUrl,
                (it, v) -> {
                    it.setJdbcUrl(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> driverClassName(final String driverClassName) {
            return MutatorStage.of(
                driverClassName,
                (it, v) -> {
                    it.setDriverClassName(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> username(final String username) {
            return MutatorStage.of(
                username,
                (it, v) -> {
                    it.setUsername(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> password(final String password) {
            return MutatorStage.of(
                password,
                (it, v) -> {
                    it.setPassword(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> maximumPoolSize(final int maximumPoolSize) {
            return MutatorStage.of(
                maximumPoolSize,
                (it, v) -> {
                    it.setMaximumPoolSize(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> minimumIdle(final int minimumIdle) {
            return MutatorStage.of(
                minimumIdle,
                (it, v) -> {
                    it.setMinimumIdle(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> idleTimeout(final long idleTimeoutMs) {
            return MutatorStage.of(
                idleTimeoutMs,
                (it, v) -> {
                    it.setIdleTimeout(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> connectionTimeout(final long connectionTimeoutMs) {
            return MutatorStage.of(
                connectionTimeoutMs,
                (it, v) -> {
                    it.setConnectionTimeout(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> poolName(final String poolName) {
            return MutatorStage.of(
                poolName,
                (it, v) -> {
                    it.setPoolName(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> threadFactory(final ThreadFactory threadFactory) {
            return MutatorStage.of(
                threadFactory,
                (it, v) -> {
                    it.setThreadFactory(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> connectionTestQuery(final String testQuery) {
            return MutatorStage.of(
                testQuery,
                (it, v) -> {
                    it.setConnectionTestQuery(v);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> dataSourceProperty(final String propertyName, String value) {
            return MutatorStage.of2(
                propertyName, value,
                (it, v1, v2) -> {
                    it.addDataSourceProperty(v1, v2);
                    return it;
                }
            );
        }

        public MutatorStage<HikariConfig> autoCommit(final boolean autoCommit) {
            return MutatorStage.of(
                autoCommit,
                (it, v) -> {
                    it.setAutoCommit(v);
                    return it;
                }
            );
        }
    }
}
