package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.LikeCreateDTO;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.service.LikeService;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
  private final LikeService likeService;

  @PostMapping("/create")
  public LikeDTO create(@RequestBody LikeCreateDTO dto) {
    return likeService.create(dto);
  }

  @GetMapping("{id}")
  public int getLikesCount(@PathVariable long id) {
    return likeService.getLikesCountForPost(id);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable long id) {
    likeService.deleteById(id);
  }
}
