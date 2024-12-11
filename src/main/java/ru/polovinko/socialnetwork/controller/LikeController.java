package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.LikeCreateDTO;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.dto.LikeSearchDTO;
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

  @PostMapping("/search")
  public Page<LikeDTO> search(@RequestBody LikeSearchDTO dto, Pageable pageable) {
    return likeService.search(dto, pageable);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable long id) {
    likeService.delete(id);
  }
}
