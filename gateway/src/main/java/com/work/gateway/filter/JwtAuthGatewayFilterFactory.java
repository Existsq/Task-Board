package com.work.gateway.filter;

import com.work.gateway.filter.JwtAuthGatewayFilterFactory.Config;
import com.work.gateway.util.JwtUtil;
import java.net.URI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {

  private final JwtUtil jwtUtil;

  public JwtAuthGatewayFilterFactory(JwtUtil jwtUtil) {
    super(Config.class);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty("Authorization");
      if (authHeaders.isEmpty()) {
        return redirectToLogin(exchange, config.getLoginRedirectUrl());
      }

      String token = authHeaders.getFirst().replace("Bearer ", "");

      if (!jwtUtil.validateToken(token)) {
        return redirectToLogin(exchange, config.getLoginRedirectUrl());
      }

      String uuid = jwtUtil.getClaim(token, config.getClaim());

      if (uuid == null) {
        return redirectToLogin(exchange, config.getLoginRedirectUrl());
      }

      ServerHttpRequest mutatedRequest =
          exchange.getRequest().mutate().header(config.getHeaderName(), uuid).build();

      log.info("uuid claim from token - {}", uuid);

      return chain.filter(exchange.mutate().request(mutatedRequest).build());
    };
  }

  private Mono<Void> redirectToLogin(ServerWebExchange exchange, String redirectUrl) {
    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
    exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
    log.info("Пользователь был перенаправлен на страницу авторизации");
    return exchange.getResponse().setComplete();
  }

  @Setter
  @Getter
  public static class Config {
    private String headerName = "X-User-Id";
    private String claim = "uuid";
    private String loginRedirectUrl = "http://localhost:3000/login";
  }
}
