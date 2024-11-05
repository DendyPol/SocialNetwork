package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.FriendRequestCreateDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;

import java.util.List;

public interface FriendRequestService {
  FriendRequestDTO create(FriendRequestCreateDTO dto);

  FriendRequestDTO update(long requestId);

  void delete(long requestId);

  List<FriendRequestDTO> getFriendRequestForUser(long userId);
}
