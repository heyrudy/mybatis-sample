package com.heyrudy.mybatissample.context;

import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Arrays;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "db.secret")
@Validated
public record DbSecretProperties(
    @NotBlank(message = "Host cannot be empty")
    String host,
    @NotNull
    @Pattern(regexp = "^\\d+$", message = "Port must be numeric")
    @Range(min = 1, max = 65535, message = "Port must be between 1 and 65535")
    String port,
    @NotBlank(message = "Protocol cannot be empty")
    String protocol,
    @NotBlank(message = "Schema cannot be empty")
    String schema,
    @NotBlank(message = "Username cannot be empty")
    String username,
    @NotNull
    char[] password) implements IDbSecretProperties {

    public String getJdbcUrl() {
        return "%s://%s:%s/%s".formatted(protocol, host, port, schema);
    }

    @PreDestroy
    public void clearPassword() {
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }

}