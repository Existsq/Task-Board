package com.work.auth.controller.advice;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
  private final String error;
  private final String description;
}
