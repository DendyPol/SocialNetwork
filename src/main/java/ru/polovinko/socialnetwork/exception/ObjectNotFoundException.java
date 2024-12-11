package ru.polovinko.socialnetwork.exception;

import java.util.NoSuchElementException;

public class ObjectNotFoundException extends NoSuchElementException {
  public ObjectNotFoundException(String message) {
    super(message);
  }
}
