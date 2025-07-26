package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.UtilsModule.MutatorOption;
import com.zaxxer.hikari.HikariConfig;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;

public enum HikariConfigBuilder {
    INSTANCE;

    @SafeVarargs
    public static HikariConfig of(MutatorOption<HikariConfig>... options) {
        return Arrays.stream(options)
            .filter(Objects::nonNull)
            .reduce(new HikariConfig(), (model, option) -> option.apply(model), (a, b) -> a);
    }

    public enum HikariConfigMutatorOptions {
        INSTANCE;

        public MutatorOption<HikariConfig> jdbcUrl(String jdbcUrl) {
            return MutatorOption.of(
                jdbcUrl,
                (it, v) -> {
                    it.setJdbcUrl(jdbcUrl);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> driverClassName(String driverClassName) {
            return MutatorOption.of(
                driverClassName,
                (it, v) -> {
                    it.setDriverClassName(driverClassName);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> username(String username) {
            return MutatorOption.of(
                username,
                (it, v) -> {
                    it.setUsername(username);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> password(String password) {
            return MutatorOption.of(
                password,
                (it, v) -> {
                    it.setPassword(password);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> maximumPoolSize(int maximumPoolSize) {
            return MutatorOption.of(
                maximumPoolSize,
                (it, v) -> {
                    it.setMaximumPoolSize(maximumPoolSize);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> minimumIdle(int minimumIdle) {
            return MutatorOption.of(
                minimumIdle,
                (it, v) -> {
                    it.setMinimumIdle(minimumIdle);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> idleTimeout(long idleTimeoutMs) {
            return MutatorOption.of(
                idleTimeoutMs,
                (it, v) -> {
                    it.setIdleTimeout(idleTimeoutMs);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> connectionTimeout(long connectionTimeoutMs) {
            return MutatorOption.of(
                connectionTimeoutMs,
                (it, v) -> {
                    it.setConnectionTimeout(connectionTimeoutMs);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> poolName(String poolName) {
            return MutatorOption.of(
                poolName,
                (it, v) -> {
                    it.setPoolName(poolName);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> threadFactory(ThreadFactory threadFactory) {
            return MutatorOption.of(
                threadFactory,
                (it, v) -> {
                    it.setThreadFactory(threadFactory);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> connectionTestQuery(String testQuery) {
            return MutatorOption.of(
                testQuery,
                (it, v) -> {
                    it.setConnectionTestQuery(testQuery);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> dataSourceProperty(String propertyName, String value) {
            return MutatorOption.of2(
                propertyName, value,
                (it, v1, v2) -> {
                    it.addDataSourceProperty(propertyName, value);
                    return it;
                }
            );
        }

        public MutatorOption<HikariConfig> autoCommit(boolean autoCommit) {
            return MutatorOption.of(
                autoCommit,
                (it, v) -> {
                    it.setAutoCommit(autoCommit);
                    return it;
                }
            );
        }
    }
}