package ru.polovinko.socialnetwork.exception;

public class EntityExistException extends IllegalStateException {
  public EntityExistException(String message) {
    super(message);
  }
}
