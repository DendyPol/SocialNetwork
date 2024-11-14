package ru.polovinko.socialnetwork.exception;

public class AlreadyExistException extends IllegalStateException {
  public AlreadyExistException(String message) {
    super(message);
  }
}
