package com.work.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenService {

  private final long accessTokenExpiration;

  private final Algorithm algorithm;

  public JwtTokenService(
      @Value("${jwt.access-token.expiration:86400000}") long accessTokenExpiration,
      @Value("${jwt.secret}") String secret) {
    this.accessTokenExpiration = accessTokenExpiration;
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String generateToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + accessTokenExpiration);

    String token =
        JWT.create()
            .withSubject(subject)
            .withIssuedAt(now)
            .withExpiresAt(exp)
            .withPayload(claims)
            .sign(algorithm);
    log.info("Сгенерирован новый токен для пользователя с почтой {}", subject);
    return token;
  }

  public String extractEmail(String jwt) {
    try {
      DecodedJWT decoded = JWT.require(algorithm).build().verify(jwt);
      return decoded.getSubject();
    } catch (JWTVerificationException e) {
      log.warn("Не удалось извлечь имя пользователя из токена: {}", e.getMessage());
      return null;
    }
  }

  public boolean validateToken(String jwt, UserDetails userDetails) {
    try {
      DecodedJWT decoded = JWT.require(algorithm).build().verify(jwt);

      String username = decoded.getSubject();
      Date expiration = decoded.getExpiresAt();

      return username != null
          && username.equals(userDetails.getUsername())
          && expiration.after(new Date());
    } catch (JWTVerificationException e) {
      log.warn("Невалидный JWT: {}", e.getMessage());
      return false;
    }
  }
}
