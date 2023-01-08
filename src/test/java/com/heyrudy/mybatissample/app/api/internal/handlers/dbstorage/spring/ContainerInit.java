package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Slf4j
public class ContainerInit implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static Postgres11TC postgres11TC;

    static {
        postgres11TC = Postgres11TC.getInstance();
        postgres11TC.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + postgres11TC.getJdbcUrl(),
                "spring.datasource.username=" + postgres11TC.getUsername(),
                "spring.datasource.password=" + postgres11TC.getPassword(),
                "db.host=" + postgres11TC.getHost(),
                "db.port=" + postgres11TC.getMappedPort(Postgres11TC.POSTGRESQL_PORT),
                "db.name=" + postgres11TC.getDatabaseName(),
                "db.username=" + postgres11TC.getUsername(),
                "db.password=" + postgres11TC.getPassword()
        );
    }
}
