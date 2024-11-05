package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.LikeCreateDTO;
import ru.polovinko.socialnetwork.dto.LikeDTO;

public interface LikeService {
  LikeDTO create(LikeCreateDTO dto);

  void deleteById(long id);

  int getLikesCountForPost(long postId);
}
