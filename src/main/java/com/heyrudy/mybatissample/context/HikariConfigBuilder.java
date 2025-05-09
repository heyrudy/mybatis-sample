package com.heyrudy.mybatissample.context;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class HikariConfigBuilder {

    private final HikariConfig config = new HikariConfig();

    private HikariConfigBuilder() {
        super();
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