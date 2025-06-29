package com.work.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequest {
  @NotBlank(message = "Email обязателен")
  @Email(message = "Email имеет неверный формат")
  private String email;

  @NotBlank(message = "Password обязателен")
  private String password;
}
