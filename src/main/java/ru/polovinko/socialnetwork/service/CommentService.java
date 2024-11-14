package ru.polovinko.socialnetwork.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.CommentCreateDTO;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentSearchDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;

public interface CommentService {
  Page<CommentDTO> search(CommentSearchDTO dto, Pageable pageable);

  CommentDTO create(CommentCreateDTO dto);

  void delete(long id);

  CommentDTO update(CommentUpdateDTO dto);
}
