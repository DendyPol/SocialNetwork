package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;
import ru.polovinko.socialnetwork.service.FriendRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-request")
@RequiredArgsConstructor
public class FriendRequestController {
  private final FriendRequestService friendRequestService;

  @PostMapping("/{userid}/to/{friendId}")
  public FriendRequestDTO sendFriendRequest(@PathVariable long userid, @PathVariable long friendId) {
    return friendRequestService.sendFriendRequest(userid, friendId);
  }

  @PutMapping("/{requestId}/accept")
  public FriendRequestDTO acceptFriendRequest(@PathVariable long requestId) {
    return friendRequestService.acceptFriendRequest(requestId);
  }

  @DeleteMapping("/{requestId}/reject")
  public void rejectFriendRequest(@PathVariable long requestId) {
    friendRequestService.rejectFriendRequest(requestId);
  }

  @GetMapping("/user/{userId}")
  public List<FriendRequestDTO> getFriendRequestFoUser(@PathVariable long userId) {
    return friendRequestService.getFriendRequestForUser(userId);
  }
}
