package com.work.auth.controller.advice;

import com.work.auth.exception.NotValidCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.work.auth.controller")
public class ControllerAdvice {

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(NotValidCredentialsException.class)
  public ErrorResponse handleNotValidCredentials(NotValidCredentialsException e) {
    log.info("Ошибка авторизации: {}", e.getMessage());
    return ErrorResponse.builder()
        .error("Ошибка в переданных данных")
        .description(e.getMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
    String errorMessage =
        e.getBindingResult().getAllErrors().stream()
            .findFirst()
            .map(ObjectError::getDefaultMessage)
            .orElse("Неверные данные");
    log.info("Ошибка валидации входных данных: {}", errorMessage);

    return ErrorResponse.builder().error("Ошибка валидации").description(errorMessage).build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleUnknownException(Exception e) {
    log.error("Непредвиденная ошибка: ", e);

    return ErrorResponse.builder()
        .error("Внутренняя ошибка сервера")
        .description("Что-то пошло не так. Мы уже работаем над этим.")
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(AuthenticationException.class)
  public ErrorResponse handleAuthenticationException(AuthenticationException e) {
    log.info("Ошибка аутентификации: {}", e.getMessage(), e);

    return ErrorResponse.builder()
        .error("Ошибка аутентификации")
        .description(e.getMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadCredentialsException.class)
  public ErrorResponse handleBadCredentialsException(BadCredentialsException e) {
    log.info("Ошибка аутентификации: {}", e.getMessage(), e);

    return ErrorResponse.builder()
        .error("Ошибка аутентификации")
        .description(e.getMessage())
        .build();
  }
}
