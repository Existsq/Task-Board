package com.work.task.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.work.task.exception.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.work.task.controller")
public class ControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ErrorResponse handleMissingHeader(MissingRequestHeaderException e) {
    String message = "Отсутствует обязательный header: " + e.getHeaderName();
    log.warn("Отсутствует header X-User-Id: {}", e.getMessage());

    return new ErrorResponse(message, e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    Throwable cause = e.getCause();
    String message = "Неверный формат данных";

    if (cause instanceof InvalidFormatException invalidFormatException) {
      Class<?> targetType = invalidFormatException.getTargetType();
      if (targetType != null
          && targetType.isEnum()
          && targetType.equals(com.work.task.model.TaskStatus.class)) {
        String invalidValue = invalidFormatException.getValue().toString();
        message =
            "Недопустимое значение для поля статуса: '"
                + invalidValue
                + "'. Допустимые значения: NEW, COMPLETED, IN_PROGRESS";
      }
    }

    log.warn("Ошибка парсинга JSON: {}", e.getMessage());
    return new ErrorResponse("Ошибка входных данных", message);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
    String errorMessage =
        e.getBindingResult().getAllErrors().stream()
            .findFirst()
            .map(ObjectError::getDefaultMessage)
            .orElse("Неверные входные данные");

    log.info("Ошибка валидации входных данных: {}", errorMessage);
    return new ErrorResponse("Ошибка валидации", errorMessage);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(TaskNotFoundException.class)
  public ErrorResponse handleTaskNotFoundException(TaskNotFoundException e) {
    log.warn("Задача не найдена: {}", e.getMessage());
    return new ErrorResponse("Ошибка при получении", e.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleGeneralException(Exception e) {
    log.error("Необработанная ошибка: ", e);
    return new ErrorResponse("Внутренняя ошибка сервера", "Попробуйте позже");
  }
}
