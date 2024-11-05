package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.PostCreateDTO;
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostSearchDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;
import ru.polovinko.socialnetwork.service.PostService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;

  @GetMapping("/search")
  public Page<PostDTO> search(@RequestBody PostSearchDTO dto, Pageable pageable) {
    return postService.search(dto, pageable);
  }

  @GetMapping("{id}")
  public Optional<PostDTO> findById(@PathVariable long id) {
    return postService.findById(id);
  }

  @PostMapping
  public PostDTO create(@RequestBody PostCreateDTO dto) {
    return postService.create(dto);
  }

  @DeleteMapping("{id}")
  public void deleteById(@PathVariable long id) {
    postService.deleteById(id);
  }

  @PutMapping
  public PostDTO update(@RequestBody PostUpdateDTO dto) {
    return postService.update(dto);
  }
}
