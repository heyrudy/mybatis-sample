package com.heyrudy.mybatissample.context;

import io.vavr.control.Option;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;

public class SpringAppScopedDependencyLocator
    implements AppScopedDependencyLocator {

    private final ApplicationContext applicationContext;

    public SpringAppScopedDependencyLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getBeanOrMock(Class<T> beanClass, Option<Supplier<T>> fallback) {
        return applicationContext
            .getBeanProvider(beanClass)
            .getIfAvailable(() -> fallback.map(Supplier::get).getOrNull());
    }
}
