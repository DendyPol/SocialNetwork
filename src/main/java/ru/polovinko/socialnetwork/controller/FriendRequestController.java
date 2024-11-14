package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.FriendRequestCreateDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestSearchDTO;
import ru.polovinko.socialnetwork.service.FriendRequestService;

@RestController
@RequestMapping("/api/v1/friend-request")
@RequiredArgsConstructor
public class FriendRequestController {
  private final FriendRequestService friendRequestService;

  @PostMapping("/create")
  public FriendRequestDTO create(@RequestBody FriendRequestCreateDTO dto) {
    return friendRequestService.create(dto);
  }

  @PutMapping("{id}")
  public FriendRequestDTO update(@PathVariable long id) {
    return friendRequestService.update(id);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable long id) {
    friendRequestService.delete(id);
  }

  @PostMapping("/search")
  public Page<FriendRequestDTO> search(@RequestBody FriendRequestSearchDTO dto, Pageable pageable) {
    return friendRequestService.search(dto, pageable);
  }
}
