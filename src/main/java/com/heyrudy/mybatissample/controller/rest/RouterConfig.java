package com.heyrudy.mybatissample.controller.rest;

//import com.heyrudy.mybatissample.controller.rest.CityController;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.vavr.control.Either;
//import io.vavr.control.Try;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//import java.util.function.Function;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.servlet.function.HandlerFilterFunction;
//import org.springframework.web.servlet.function.RouterFunction;
//import org.springframework.web.servlet.function.RouterFunctions;
//import org.springframework.web.servlet.function.ServerRequest;
//import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterConfig {

//    public static final Logger logger = LoggerFactory.getLogger(RouterConfig.class);
//    private static final String SECRET_KEY = "your-secret-key"; // Use a secure method in production

//    @Bean
//    public RouterFunction<ServerResponse> routerFunctionWithFilters(
//        RouterFunction<ServerResponse> routerFunction) {
//        return routerFunction
//            .filter(mdcFilter())
//            .filter(loggingFilter())
//            .filter(jwtAuthenticationFilter())
//            .filter(metricsCollectionFilter());
//    }

//    // Example routes
//    @Bean
//    public RouterFunction<ServerResponse> routes(CityController cityController) {
//        return RouterFunctions.route()
//            .POST("api/v1/cities", cityController::createCity)
//            .GET("api/v1/cities", cityController::findCities)
//            .GET("api/v1/cities/{cityId}", cityController::findCityById)
//            .GET("api/v1/cities/download", cityController::downloadCityPdfReport)
//            .build();
//    }

//    // JWT Authentication Filter
//    private HandlerFilterFunction<ServerResponse, ServerResponse> jwtAuthenticationFilter() {
//        return (request, next) -> {
//            // Function to validate JWT token
//            Function<String, Either<String, Claims>> validateToken = token ->
//                Try.of(() -> Jwts.parser()
//                        .setSigningKey(SECRET_KEY)
//                        .parseClaimsJws(token)
//                        .getBody())
//                    .toEither()
//                    .mapLeft(ex -> "Invalid JWT token: " + ex.getMessage());
//
//            // Function to extract JWT token from request
//            Function<ServerRequest, Either<String, String>> extractToken =
//                req ->
//                req.headers().firstHeader("Authorization")
//                    .map(header -> header.startsWith("Bearer ") ?
//                        Either.<String, String>right(header.substring(7)) :
//                        Either.<String, String>left("Invalid Authorization header format"))
//                    .orElse(Either.left("Authorization header is missing"));
//
//            // Skip authentication for permitted paths
//            String path = request.path();
//            List<String> openPaths = List.of("/public", "/auth/login", "/health");
//
//            boolean isOpenPath = openPaths.stream().anyMatch(path::startsWith);
//
//            return isOpenPath
//                ? next.handle(request)
//                : extractToken.apply(request)
//                .flatMap(validateToken)
//                .fold(
//                    errorMsg -> {
//                        logger.error(errorMsg);
//                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
//                            .body(errorMsg);
//                    },
//                    claims -> {
//                        // Create a new request with authentication attributes
//                        ServerRequest authenticatedRequest =
//                            new ServerRequestWrapper(request, claims);
//                        return next.handle(authenticatedRequest);
//                    }
//                );
//
//        };
//    }
//
//    // MDC Filter
//    private HandlerFilterFunction<ServerResponse, ServerResponse> mdcFilter() {
//        return (request, next) -> {
//            String requestId = UUID.randomUUID().toString();
//
//            // Set MDC context using vavr's Try
//            Try.run(() -> {
//                MDC.put("requestId", requestId);
//                MDC.put("path", request.path());
//                MDC.put("method", request.method().name());
//
//                // Add user info to MDC if available
//                request.attribute("userId")
//                    .ifPresent(userId -> MDC.put("userId", userId.toString()));
//            });
//
//            try {
//                return next.handle(request);
//            } finally {
//                MDC.clear();
//            }
//        };
//    }
//
//    // Logging Filter
//    private HandlerFilterFunction<ServerResponse, ServerResponse> loggingFilter() {
//        return (request, next) -> {
//            logger.info("Request received: {} {}", request.method().name(), request.path());
//
//            ServerResponse response = next.handle(request);
//
//            logger.info("Response status: {}", response.statusCode());
//
//            return response;
//        };
//    }
//
//    // Metrics Collection Filter
//    private HandlerFilterFunction<ServerResponse, ServerResponse> metricsCollectionFilter() {
//        return (request, next) -> {
//            Instant start = Instant.now();
//
//            ServerResponse response = next.handle(request);
//
//            // Using vavr's Try to calculate and log response time
//            Try.run(() -> {
//                Duration responseTime = Duration.between(start, Instant.now());
//
//                logger.info("Response time: {} ms for {} {} (status: {})",
//                    responseTime.toMillis(),
//                    request.method().name(),
//                    request.path(),
//                    response.statusCode().value());
//
//                // Here you would typically send metrics to your metrics collection system
//            });
//
//            return response;
//        };
//    }

//  // Custom ServerRequest wrapper to store authenticated user details
//    private static class ServerRequestWrapper implements ServerRequest {
//
//        private final ServerRequest delegate;
//        private final Claims claims;
//
//        ServerRequestWrapper(ServerRequest delegate, Claims claims) {
//            this.delegate = delegate;
//            this.claims = claims;
//
//            // Add authentication attributes to the request
//            delegate.attributes().put("userId", claims.getSubject());
//            delegate.attributes().put("roles",
//                claims.get("roles", List.class) != null ?
//                    claims.get("roles", List.class) :
//                    List.of());
//        }
//
//        // Delegate all methods to the original request
//        @Override
//        public String path() {
//            return delegate.path();
//        }
//
//        @Override
//        public Headers headers() {
//            return delegate.headers();
//        }
//
//        @Override
//        public <T> T body(Class<T> bodyType) {
//            return delegate.body(bodyType);
//        }
//
//        // Implement all other methods from ServerRequest by delegating
//        // Only showing a few for brevity
//        @Override
//        public Optional<String> param(String name) {
//            return delegate.param(name);
//        }
//
//        @Override
//        public List<String> params(String name) {
//            return delegate.params(name);
//        }
//
//        @Override
//        public Optional<Object> attribute(String name) {
//            return delegate.attribute(name);
//        }
//
//        @Override
//        public Map<String, Object> attributes() {
//            return delegate.attributes();
//        }
//
//        @Override
//        public HttpMethod method() {
//            return delegate.method();
//        }
//
//        @Override
//        public URI uri() {
//            return delegate.uri();
//        }
//
//        @Override
//        public String pathVariable(String name) {
//            return delegate.pathVariable(name);
//        }
//
//        @Override
//        public Map<String, String> pathVariables() {
//            return delegate.pathVariables();
//        }
//
//        // Implement all remaining methods...
//    }

//  // Custom ServerRequest wrapper to store request ID
//    private static class MdcServerRequestWrapper extends ServerRequestWrapper {
//
//        MdcServerRequestWrapper(ServerRequest delegate, String requestId) {
//            super(delegate, null); // No claims here
//            delegate.attributes().put("requestId", requestId);
//        }
//    }
}