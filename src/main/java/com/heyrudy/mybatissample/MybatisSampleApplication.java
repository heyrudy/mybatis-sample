package com.heyrudy.mybatissample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class
    }
)
public class MybatisSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisSampleApplication.class, args);
    }
}
