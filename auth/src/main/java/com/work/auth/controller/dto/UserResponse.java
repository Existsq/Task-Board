package com.work.auth.controller.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
  private String email;
  private String uuid;
}