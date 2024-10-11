package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;

import java.util.List;

public interface CommentService {
  List<CommentDTO> findAllCommentsForPost(long postId);

  CommentDTO create(long postId, CommentDTO commentDTO);

  void deleteById(long id);

  CommentDTO update(long id, CommentUpdateDTO dto);
}
