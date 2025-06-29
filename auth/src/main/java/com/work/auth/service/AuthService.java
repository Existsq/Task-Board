package com.work.auth.service;

import com.work.auth.controller.dto.UserResponse;
import com.work.auth.exception.NotValidCredentialsException;
import com.work.auth.model.AuthUser;
import com.work.auth.repository.AuthUserRepository;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final AuthUserRepository repository;
  private final JwtTokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  public String register(final String email, final String password) {
    if (repository.findByEmail(email).isPresent()) {
      log.info("Попытка регистрации с уже существующим email: {}", email);
      throw new NotValidCredentialsException("Email " + email + " уже используется");
    }

    AuthUser user = new AuthUser();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setCreatedAt(Instant.now());

    repository.save(user);

    Optional<AuthUser> savedUser = repository.findByEmail(user.getEmail());

    if (savedUser.isEmpty()) {
      throw new RuntimeException("Что-то пошло нет. Уже чиним");
    }

    String token =
        tokenService.generateToken(
            Map.of("uuid", savedUser.get().getUuid().toString()), savedUser.get().getEmail());
    log.info("Зарегистрирован новый пользователь: {}", email);
    return token;
  }

  @Transactional(readOnly = true)
  public UserResponse getUserByEmail(String email) {
    Optional<AuthUser> user = repository.findByEmail(email);
    if (user.isEmpty()) {
      throw new NotValidCredentialsException("Пользователь с email " + email + " не найден");
    }

    UserResponse response = new UserResponse();
    response.setEmail(user.get().getEmail());
    response.setUuid(user.get().getUuid().toString());
    return response;
  }
}
