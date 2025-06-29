package com.work.auth.exception;

public class NotValidCredentialsException extends RuntimeException {
  public NotValidCredentialsException(String message) {
    super(message);
  }
}
