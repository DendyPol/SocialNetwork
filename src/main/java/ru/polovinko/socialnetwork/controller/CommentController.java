package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.CommentCreateDTO;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentSearchDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;
import ru.polovinko.socialnetwork.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @PostMapping("/search")
  public Page<CommentDTO> search(@RequestBody CommentSearchDTO dto, Pageable pageable) {
    return commentService.search(dto, pageable);
  }

  @PostMapping("/create")
  public CommentDTO create(@RequestBody CommentCreateDTO dto) {
    return commentService.create(dto);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable long id) {
    commentService.delete(id);
  }

  @PutMapping
  public CommentDTO update(@RequestBody CommentUpdateDTO dto) {
    return commentService.update(dto);
  }
}
