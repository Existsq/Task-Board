package com.work.auth.controller;

import com.work.auth.controller.dto.AuthenticationRequest;
import com.work.auth.controller.dto.AuthenticationResponse;
import com.work.auth.controller.dto.UserResponse;
import com.work.auth.service.AuthService;
import com.work.auth.service.JwtTokenService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtTokenService tokenService;
  private final AuthenticationManager authenticationManager;

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getCurrentUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserResponse user = authService.getUserByEmail(userDetails.getUsername());

    return ResponseEntity.ok(user);
  }

  @PostMapping(
      value = "/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    UserResponse user = authService.getUserByEmail(userDetails.getUsername());

    String jwt =
        tokenService.generateToken(
            Map.of("uuid", user.getUuid(), "email", user.getEmail()), user.getEmail());
    return new AuthenticationResponse(jwt);
  }

  @PostMapping(
      value = "/register",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AuthenticationResponse register(@Valid @RequestBody AuthenticationRequest req) {
    String email = req.getEmail();
    String password = req.getPassword();

    return new AuthenticationResponse(authService.register(email, password));
  }
}
