package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;

  @GetMapping
  public List<PostDTO> findAll() {
    return postService.findAll();
  }

  @GetMapping("{id}")
  public PostDTO findById(@PathVariable long id) {
    return postService.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
  }

  @PostMapping
  public PostDTO create(@RequestBody PostDTO dto) {
    return postService.create(dto);
  }

  @DeleteMapping("{id}")
  public void deleteById(@PathVariable long id) {
    postService.deleteById(id);
  }

  @PutMapping("{id}")
  public PostDTO update(@PathVariable long id, @RequestBody PostUpdateDTO dto) {
    return postService.update(id, dto);
  }
}
