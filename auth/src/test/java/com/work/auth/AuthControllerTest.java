package com.work.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import com.work.auth.controller.AuthController;
import com.work.auth.controller.dto.AuthenticationRequest;
import com.work.auth.controller.dto.AuthenticationResponse;
import com.work.auth.controller.dto.UserResponse;
import com.work.auth.service.AuthService;
import com.work.auth.service.JwtTokenService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

class AuthControllerTest {

  @Mock private AuthService authService;
  @Mock private JwtTokenService tokenService;
  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private AuthController authController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getCurrentUser_WithUserDetails_ReturnsUserResponse() {
    String email = "test@example.com";
    UserResponse userResponse = new UserResponse();
    userResponse.setEmail(email);
    userResponse.setUuid("uuid-1234");

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(email);
    when(authService.getUserByEmail(email)).thenReturn(userResponse);

    ResponseEntity<UserResponse> response = authController.getCurrentUser(userDetails);

    assertEquals(OK, response.getStatusCode());
    assertEquals(userResponse, response.getBody());
  }

  @Test
  void getCurrentUser_WithoutUserDetails_ReturnsUnauthorized() {
    ResponseEntity<UserResponse> response = authController.getCurrentUser(null);
    assertEquals(UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void login_ValidRequest_ReturnsAuthenticationResponse() {
    String email = "test@example.com";
    String password = "password123";
    String token = "jwt-token";

    AuthenticationRequest request = new AuthenticationRequest();
    request.setEmail(email);
    request.setPassword(password);

    Authentication authentication = mock(Authentication.class);
    UserDetails userDetails = mock(UserDetails.class);
    UserResponse userResponse = new UserResponse();
    userResponse.setEmail(email);
    userResponse.setUuid("uuid-1234");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn(email);
    when(authService.getUserByEmail(email)).thenReturn(userResponse);
    when(tokenService.generateToken(
            eq(Map.of("uuid", userResponse.getUuid(), "email", userResponse.getEmail())),
            eq(email)))
        .thenReturn(token);

    AuthenticationResponse response = authController.login(request);

    assertNotNull(response);
    assertEquals(token, response.token());
  }

  @Test
  void register_ValidRequest_ReturnsAuthenticationResponse() {
    String email = "test@example.com";
    String password = "password123";
    String token = "jwt-token";

    AuthenticationRequest request = new AuthenticationRequest();
    request.setEmail(email);
    request.setPassword(password);

    when(authService.register(email, password)).thenReturn(token);

    AuthenticationResponse response = authController.register(request);

    assertNotNull(response);
    assertEquals(token, response.token());
  }
}
