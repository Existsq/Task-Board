package com.work.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.work.auth.controller.dto.UserResponse;
import com.work.auth.exception.NotValidCredentialsException;
import com.work.auth.model.AuthUser;
import com.work.auth.repository.AuthUserRepository;
import com.work.auth.service.AuthService;
import com.work.auth.service.JwtTokenService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTest {

  @Mock
  private AuthUserRepository repository;

  @Mock
  private JwtTokenService tokenService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthService authService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

//  @Test
//  void register_shouldCreateUserAndReturnToken() {
//    String email = "test@example.com";
//    String rawPassword = "password123";
//    String encodedPassword = "encodedPassword123";
//    String expectedToken = "jwt.token.value";
//
//    when(repository.findByEmail(email)).thenReturn(Optional.empty());
//    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
//    when(tokenService.generateToken(anyMap(), eq(email))).thenReturn(expectedToken);
//
//    String token = authService.register(email, rawPassword);
//
//    assertEquals(expectedToken, token);
//    verify(repository).save(argThat(user ->
//        user.getEmail().equals(email)
//            && user.getPassword().equals(encodedPassword)
//            && user.getCreatedAt() != null
//    ));
//    verify(tokenService).generateToken(anyMap(), eq(email));
//  }

  @Test
  void register_shouldThrowException_whenEmailExists() {
    String email = "existing@example.com";

    when(repository.findByEmail(email)).thenReturn(Optional.of(new AuthUser()));

    NotValidCredentialsException ex = assertThrows(
        NotValidCredentialsException.class,
        () -> authService.register(email, "anyPassword")
    );

    assertTrue(ex.getMessage().contains("уже используется"));
    verify(repository, never()).save(any());
  }

  @Test
  void getUserByEmail_shouldReturnUserResponse() {
    String email = "test@example.com";
    UUID uuid = UUID.randomUUID();

    AuthUser user = new AuthUser();
    user.setEmail(email);
    user.setUuid(uuid);
    when(repository.findByEmail(email)).thenReturn(Optional.of(user));

    UserResponse response = authService.getUserByEmail(email);

    assertEquals(email, response.getEmail());
    assertEquals(uuid.toString(), response.getUuid());
  }

  @Test
  void getUserByEmail_shouldThrowException_whenUserNotFound() {
    String email = "unknown@example.com";
    when(repository.findByEmail(email)).thenReturn(Optional.empty());

    NotValidCredentialsException ex = assertThrows(
        NotValidCredentialsException.class,
        () -> authService.getUserByEmail(email)
    );

    assertTrue(ex.getMessage().contains("не найден"));
  }
}