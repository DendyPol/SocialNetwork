package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.FriendRequestCreateDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;
import ru.polovinko.socialnetwork.service.FriendRequestService;

import java.util.List;

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

  @GetMapping("{id}")
  public List<FriendRequestDTO> getFriendRequestFoUser(@PathVariable long id) {
    return friendRequestService.getFriendRequestForUser(id);
  }
}
