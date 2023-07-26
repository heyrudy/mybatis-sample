package com.heyrudy.mybatissample.controller.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    public static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthConverter.class);
    private static final JwtGrantedAuthoritiesConverter JWT_GRANTED_AUTHORITIES_CONVERTER =
            new JwtGrantedAuthoritiesConverter();
    private final JwtAuthConverterProperties properties;

    public JwtAuthConverter(JwtAuthConverterProperties properties) {
        this.properties = properties;
    }

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(JWT_GRANTED_AUTHORITIES_CONVERTER.convert(jwt));

        grantedAuthorities =
                Stream.concat(
                        grantedAuthorities.stream(),
                        extractResourceRole(jwt).stream()
                ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, grantedAuthorities, getPrincipalClaimName(jwt));
    }

    private Set<? extends GrantedAuthority> extractResourceRole(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (Objects.isNull(realmAccess) && Objects.isNull(resourceAccess)) {
            LOGGER.debug("Format de jeton différent de l'attendu");
            return Set.of();
        }

        Set<String> realmRoles =
                Objects.nonNull(realmAccess) &&
                        Objects.nonNull(realmAccess.get("roles")) &&
                        realmAccess.get("roles") instanceof List<?> ls
                        ? ls.stream().map(String.class::cast).collect(Collectors.toSet())
                        : Set.of();

        Set<String> resourceRoles =
                Objects.nonNull(resourceAccess) &&
                        Objects.nonNull(resourceAccess.get(properties.resourceId())) &&
                        resourceAccess.get(properties.resourceId()) instanceof List<?> ls
                        ? ls.stream().map(String.class::cast).collect(Collectors.toSet())
                        : Set.of();

        Set<String> allRoles =
                !realmRoles.isEmpty() || !resourceRoles.isEmpty()
                        ? Stream.concat(realmRoles.stream(), resourceRoles.stream()).collect(Collectors.toSet())
                        : Set.of();
        if (allRoles.isEmpty()) {
            LOGGER.debug("Aucun rôle défini");
            return Set.of();
        } else {
            allRoles.forEach(it -> LOGGER.debug("    + {}", it));
        }

        return allRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = Optional.ofNullable(properties.principalAttribute())
                .orElse(JwtClaimNames.SUB);
        return jwt.getClaim(claimName);
    }
}
