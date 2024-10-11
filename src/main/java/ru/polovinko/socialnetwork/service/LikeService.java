package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.LikeDTO;

public interface LikeService {
  LikeDTO likePost(long postId, long userId);

  void unlikePost(long postId, long userId);

  int getLikesCountForPost(long postId);
}
