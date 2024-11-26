package ru.polovinko.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.PostCreateDTO;
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostSearchDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;

public interface PostService {
  Page<PostDTO> search(PostSearchDTO dto, Pageable pageable);

  PostDTO create(PostCreateDTO dto);

  void delete(long id);

  PostDTO update(PostUpdateDTO dto);
}
