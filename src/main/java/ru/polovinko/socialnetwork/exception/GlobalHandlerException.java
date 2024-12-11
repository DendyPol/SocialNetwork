package ru.polovinko.socialnetwork.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalHandlerException {
  @ExceptionHandler({ObjectNotFoundException.class})
  public void notFoundException(Exception e) {
    log.warn(e.getMessage());
  }

  @ExceptionHandler({EntityExistException.class})
  public void entityExistException(Exception e) {
    log.warn(e.getMessage());
  }
}
