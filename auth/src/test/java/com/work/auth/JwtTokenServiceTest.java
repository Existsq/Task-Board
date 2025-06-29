package com.work.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.work.auth.service.JwtTokenService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtTokenServiceTest {

  private JwtTokenService jwtTokenService;

  private final String secret = "test-secret-key";
  private final long expiration = 3600000L;

  @BeforeEach
  void setUp() {
    jwtTokenService = new JwtTokenService(expiration, secret);
  }

  @Test
  void generateToken_shouldReturnValidTokenWithClaims() {
    Map<String, Object> claims =
        Map.of("uuid", "123e4567-e89b-12d3-a456-426614174000", "role", "USER");
    String subject = "user@example.com";

    String token = jwtTokenService.generateToken(claims, subject);

    assertNotNull(token);
    assertFalse(token.isBlank());

    DecodedJWT decodedJWT = JWT.decode(token);

    assertEquals(subject, decodedJWT.getSubject());
    assertThat(decodedJWT.getClaim("uuid").asString())
        .isEqualTo("123e4567-e89b-12d3-a456-426614174000");
    assertThat(decodedJWT.getClaim("role").asString()).isEqualTo("USER");

    // Проверяем срок действия
    long expectedExp = decodedJWT.getIssuedAt().getTime() + expiration;
    long actualExp = decodedJWT.getExpiresAt().getTime();

    assertTrue(
        Math.abs(expectedExp - actualExp) < 1000,
        "Expiration time should be approximately now + expiration");
  }
}
