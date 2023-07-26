package com.heyrudy.mybatissample.gateway.rest.config;

import com.heyrudy.mybatissample.gateway.rest.CityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfiguration {

    @Bean
    CityClient cityClient() {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8081/cities").build();
        WebClientAdapter adapter = WebClientAdapter.forClient(client);
        HttpServiceProxyFactory serviceProxyFactory = HttpServiceProxyFactory.builder().clientAdapter(adapter).build();
        return serviceProxyFactory.createClient(CityClient.class);
    }
}
