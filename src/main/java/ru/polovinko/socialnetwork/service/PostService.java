package ru.polovinko.socialnetwork.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.PostCreateDTO;
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostSearchDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;

public interface PostService {
  Page<PostDTO> search(@Valid PostSearchDTO dto, Pageable pageable);

  PostDTO create(@Valid PostCreateDTO dto);

  void delete(long id);

  PostDTO update(@Valid PostUpdateDTO dto);
}
