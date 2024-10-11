package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.service.LikeService;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
  private final LikeService likeService;

  @PostMapping("/{postId}/users/{userId}")
  public LikeDTO likePost(@PathVariable long postId, @PathVariable long userId) {
    return likeService.likePost(postId, userId);
  }

  @GetMapping("/{postId}/count")
  public int getLikesCount(@PathVariable long postId) {
    return likeService.getLikesCountForPost(postId);
  }

  @DeleteMapping("/{postId}/users/{userId}")
  public void unlikePost(@PathVariable long postId, @PathVariable long userId) {
    likeService.unlikePost(postId, userId);
  }
}
