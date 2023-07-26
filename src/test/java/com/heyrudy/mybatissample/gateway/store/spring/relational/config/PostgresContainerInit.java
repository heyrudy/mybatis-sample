package com.heyrudy.mybatissample.gateway.store.spring.relational.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class PostgresContainerInit implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final Logger logger = LoggerFactory.getLogger(PostgresContainerInit.class);
    public static Postgres11Container postgres11Container;

    static {
        postgres11Container = Postgres11Container.getInstance();
        postgres11Container.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + postgres11Container.getJdbcUrl(),
                "spring.datasource.username=" + postgres11Container.getUsername(),
                "spring.datasource.password=" + postgres11Container.getPassword(),
                "db.host=" + postgres11Container.getHost(),
                "db.port=" + postgres11Container.getMappedPort(Postgres11Container.POSTGRESQL_PORT),
                "db.name=" + postgres11Container.getDatabaseName(),
                "db.username=" + postgres11Container.getUsername(),
                "db.password=" + postgres11Container.getPassword()
        );
    }
}
