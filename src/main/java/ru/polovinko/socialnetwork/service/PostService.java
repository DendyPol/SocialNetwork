package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
  List<PostDTO> findAll();

  Optional<PostDTO> findById(long id);

  PostDTO create(PostDTO postDTO);

  void deleteById(long id);

  PostDTO update(long id, PostUpdateDTO dto);
}
