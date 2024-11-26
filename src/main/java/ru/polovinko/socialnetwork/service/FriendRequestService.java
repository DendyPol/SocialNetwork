package ru.polovinko.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.FriendRequestCreateDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestSearchDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestUpdateDTO;

public interface FriendRequestService {
  FriendRequestDTO create(FriendRequestCreateDTO dto);

  FriendRequestDTO update(FriendRequestUpdateDTO dto);

  void delete(long requestId);

  Page<FriendRequestDTO> search(FriendRequestSearchDTO dto, Pageable pageable);
}
