package ru.polovinko.socialnetwork.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;
import ru.polovinko.socialnetwork.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @GetMapping("/post/{postId}")
  public List<CommentDTO> findAllCommentsForPost(@PathVariable long postId) {
    return commentService.findAllCommentsForPost(postId);
  }

  @PostMapping("/post/{postId}")
  public CommentDTO create(@PathVariable long postId, @Valid @RequestBody CommentDTO dto) {
    return commentService.create(postId, dto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    commentService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  public CommentDTO update(@PathVariable long id, @RequestBody CommentUpdateDTO dto) {
    return commentService.update(id, dto);
  }
}
