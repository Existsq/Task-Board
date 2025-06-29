package com.work.gateway.configuration;

import com.work.gateway.filter.JwtAuthGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

  private final JwtAuthGatewayFilterFactory jwtAuthGatewayFilterFactory;

  public GatewayConfig(JwtAuthGatewayFilterFactory jwtAuthGatewayFilterFactory) {
    this.jwtAuthGatewayFilterFactory = jwtAuthGatewayFilterFactory;
  }

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route("auth-service", r -> r.path("/api/v1/auth/**").uri("lb://AUTH-SERVICE"))
        .route(
            "task-service",
            r ->
                r.path("/api/v1/tasks/**")
                    .filters(
                        f ->
                            f.filter(
                                jwtAuthGatewayFilterFactory.apply(
                                    config -> {
                                      config.setHeaderName("X-User-Id");
                                      config.setClaim("uuid");
                                      config.setLoginRedirectUrl("http://localhost:3000/login");
                                    })))
                    .uri("lb://TASK-SERVICE"))
        .build();
  }
}
