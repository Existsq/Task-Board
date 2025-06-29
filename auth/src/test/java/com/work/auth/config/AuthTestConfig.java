package com.work.auth.config;

import com.work.auth.service.AuthService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthTestConfig {
  @Bean
  public AuthService authService() {
    return Mockito.mock(AuthService.class);
  }
}
