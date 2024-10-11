package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.repository.LikeRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public LikeDTO likePost(long postId, long userId) {
    var post = postRepository.findById(postId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", postId)));
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    if (likeRepository.existsByPostAndUser(post, user)) {
      throw new IllegalStateException("User has already liked this post");
    }
    var like = Like.builder()
      .post(post)
      .user(user)
      .build();
    likeRepository.save(like);
    return modelMapper.map(like, LikeDTO.class);
  }

  @Override
  public void unlikePost(long postId, long userId) {
    var post = postRepository.findById(postId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", postId)));
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    var like = likeRepository.findByPostAndUser(post, user)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Like not found for the given %s and %s", post, user)));
    likeRepository.delete(like);

  }

  @Override
  public int getLikesCountForPost(long postId) {
    var post = postRepository.findById(postId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", postId)));
    return likeRepository.countByPost(post);
  }
}
