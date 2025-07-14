package com.heyrudy.mybatissample.application.context;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ThreadFactory;
import javax.sql.DataSource;

public enum HikariConfigBuilder {
    INSTANCE;

    public Builder create() {
        return new Builder();
    }

    public static class Builder {

        private final HikariConfig config = new HikariConfig();

        private Builder() {
            super();
        }

        public Builder withJdbcUrl(String jdbcUrl) {
            config.setJdbcUrl(jdbcUrl);
            return this;
        }

        public Builder withDriverClassName(String driverClassName) {
            config.setDriverClassName(driverClassName);
            return this;
        }

        public Builder withUsername(String username) {
            config.setUsername(username);
            return this;
        }

        public Builder withPassword(String password) {
            config.setPassword(password);
            return this;
        }

        public Builder withMaximumPoolSize(int maximumPoolSize) {
            config.setMaximumPoolSize(maximumPoolSize);
            return this;
        }

        public Builder withMinimumIdle(int minimumIdle) {
            config.setMinimumIdle(minimumIdle);
            return this;
        }

        public Builder withIdleTimeout(long idleTimeoutMs) {
            config.setIdleTimeout(idleTimeoutMs);
            return this;
        }

        public Builder withConnectionTimeout(long connectionTimeoutMs) {
            config.setConnectionTimeout(connectionTimeoutMs);
            return this;
        }

        public Builder withPoolName(String poolName) {
            config.setPoolName(poolName);
            return this;
        }

        public Builder withThreadFactory(ThreadFactory threadFactory) {
            config.setThreadFactory(threadFactory);
            return this;
        }

        public Builder withConnectionTestQuery(String testQuery) {
            config.setConnectionTestQuery(testQuery);
            return this;
        }

        public Builder withDataSourceProperty(String propertyName, String value) {
            config.addDataSourceProperty(propertyName, value);
            return this;
        }

        public Builder withAutoCommit(boolean autoCommit) {
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