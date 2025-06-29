package com.work.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  private JWTVerifier verifier;

  @PostConstruct
  public void init() {
    verifier = JWT.require(Algorithm.HMAC256(secret)).build();
  }

  public boolean validateToken(String token) {
    try {
      verifier.verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }

  public String getClaim(String token, String claimName) {
    DecodedJWT jwt = verifier.verify(token);
    return jwt.getClaim(claimName).asString();
  }
}
