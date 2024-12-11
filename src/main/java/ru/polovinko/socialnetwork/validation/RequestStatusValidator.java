package ru.polovinko.socialnetwork.validation;

import ru.polovinko.socialnetwork.exception.EntityExistException;
import ru.polovinko.socialnetwork.model.FriendshipStatus;

public class RequestStatusValidator {
  public static void validateStatus(FriendshipStatus oldStatus, FriendshipStatus newStatus) {
    if (newStatus == FriendshipStatus.PENDING) {
      throw new EntityExistException("Cannot update a request to PENDING status!");
    }
    if (oldStatus == FriendshipStatus.ACCEPTED && newStatus == FriendshipStatus.REJECTED) {
      throw new EntityExistException("Cannot change status from ACCEPTED to REJECTED!");
    }
  }
}
