package com.heyrudy.mybatissample.gateway.config;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import io.vavr.control.Option;
import java.util.function.Supplier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

//    private final ApplicationContext applicationContext;
//
//    TestConfig(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    @Bean
    @Primary
    public AppScopedDependencyLocator springTestAppScopedDependencyLocator() {
        return SpringTestAppScopedDependencyLocator.INSTANCE;
    }

    public enum SpringTestAppScopedDependencyLocator
        implements AppScopedDependencyLocator {
        INSTANCE;

//        private final ApplicationContext applicationContext;
//
//        public SpringTestAppScopedDependencyLocator(ApplicationContext applicationContext) {
//            this.applicationContext = applicationContext;
//        }

        @Override
        public <T> T getBeanOrMock(Class<T> beanClass, Option<Supplier<T>> fallback) {
//            return applicationContext.getBeanProvider(beanClass).getObject();
            return fallback.map(Supplier::get).getOrNull();
        }
    }
}