package com.heyrudy.mybatissample.controller.config;

import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.config.SpringAppScopedDependencyLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class ApplicationConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    // Expose CompositionRoot as a bean
    @Bean
    public AppScopedDependencyLocator appScopedDependencyLocator() {
        return new SpringAppScopedDependencyLocator(applicationContext);
    }
}
