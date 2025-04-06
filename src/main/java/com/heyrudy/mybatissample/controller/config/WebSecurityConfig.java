package com.heyrudy.mybatissample.controller.config;

//@Configuration
//@EnableConfigurationProperties(
//    value = {
//        JwtAuthConverterProperties.class
//    }
//)
//@EnableWebSecurity
public class WebSecurityConfig {

//    public static final String ROLE_USER = "user";
//    public static final String ROLE_ADMIN = "admin";
//
//    public static final String[] AUTHORIZED_URL = {
//        "api/v1/auth/**",
//        "api/v1/auth/actuator/health/**",
//        "/v3/api-docs/**",
//        "/v3/api-docs.yaml",
//        "/swagger-ui/**",
//        "/swagger-ui.html",
//        "/swagger-ui.html",
//        "/actuator/health/**"
//    };
//
//    private final JwtAuthConverterProperties jwtAuthConverterProperties;
//
//    public WebSecurityConfiguration(JwtAuthConverterProperties jwtAuthConverterProperties) {
//        this.jwtAuthConverterProperties = jwtAuthConverterProperties;
//    }
//
//    @Bean
//    public JwtAuthConverter jwtAuthConverter() {
//        return new JwtAuthConverter(jwtAuthConverterProperties);
//    }
//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity httpSecurity)
//        throws Exception {
//        httpSecurity
//            .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
//                httpSecurityOAuth2ResourceServerConfigurer
//                    .jwt(jwtConfigurer ->
//                        jwtConfigurer
//                            .jwtAuthenticationConverter(jwtAuthConverter())));
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity)
//        throws Exception {
//        httpSecurity
//            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
//                authorizationManagerRequestMatcherRegistry
//                    .requestMatchers(OPTIONS).permitAll() // allow CORS option calls for Swagger UI
//                    .requestMatchers(AUTHORIZED_URL).permitAll()
//                    .requestMatchers(GET, "/cities").hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
//                    .anyRequest().authenticated());
//
//        httpSecurity
//            .sessionManagement(sessionManagementConfigurer ->
//                sessionManagementConfigurer
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return httpSecurity.build();
//    }
}
