package ru.polovinko.socialnetwork.service;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.CommentCreateDTO;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentSearchDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;

public interface CommentService {
  Page<CommentDTO> search(@Valid CommentSearchDTO dto, Pageable pageable);

  CommentDTO create(@Valid CommentCreateDTO dto);

  void delete(long id);

  CommentDTO update(@Valid CommentUpdateDTO dto);
}
