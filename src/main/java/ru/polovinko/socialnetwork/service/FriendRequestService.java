package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.FriendRequestDTO;

import java.util.List;

public interface FriendRequestService {
  FriendRequestDTO sendFriendRequest(long userId, long friendId);

  FriendRequestDTO acceptFriendRequest(long requestId);

  void rejectFriendRequest(long requestId);

  List<FriendRequestDTO> getFriendRequestForUser(long userId);
}
