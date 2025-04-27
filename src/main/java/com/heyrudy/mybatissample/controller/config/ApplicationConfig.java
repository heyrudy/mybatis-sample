package com.heyrudy.mybatissample.controller.config;

import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.gateway.config.AppScopedConfigLocator;
import com.heyrudy.mybatissample.gateway.config.AppScopedSecretLocator;
import com.heyrudy.mybatissample.gateway.config.SpringAppScopedConfigLocator;
import com.heyrudy.mybatissample.gateway.config.SpringAppScopedSecretLocator;
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

    // Expose Service CompositionRoot as a bean
    @Bean
    public AppScopedServiceLocator appScopedServiceLocator(
        AppScopedConfigLocator appScopedConfigLocator) {
        return new SpringAppScopedServiceLocator(appScopedConfigLocator);
    }

    // Expose Config CompositionRoot as a bean
    @Bean
    public AppScopedConfigLocator appScopedConfigLocator(
        AppScopedSecretLocator appScopedSecretLocator) {
        return new SpringAppScopedConfigLocator(appScopedSecretLocator);
    }

    // Expose Secret CompositionRoot as a bean
    @Bean
    public AppScopedSecretLocator appScopedSecretLocator() {
        return new SpringAppScopedSecretLocator(applicationContext);
    }
}
