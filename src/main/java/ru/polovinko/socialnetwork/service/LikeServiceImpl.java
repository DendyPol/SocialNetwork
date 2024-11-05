package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.LikeCreateDTO;
import ru.polovinko.socialnetwork.dto.LikeDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.LikeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
  private final LikeRepository likeRepository;
  private final PostService postService;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public LikeDTO create(LikeCreateDTO dto) {
    var postDTO = postService.findById(dto.getPostId()).get();
    var userDTO = userService.findById(dto.getUserId()).get();
    var post = modelMapper.map(postDTO, Post.class);
    var user = modelMapper.map(userDTO, User.class);
    if (likeRepository.existsByPostAndUser(post, user)) {
      throw new IllegalStateException("User has already liked this post");
    }
    var like = new Like(null, post, user);
    likeRepository.save(like);
    return modelMapper.map(like, LikeDTO.class);
  }

  @Override
  public void deleteById(long id) {
    var like = likeRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Like with ID %d not found", id)));
    likeRepository.delete(like);
  }

  @Override
  public int getLikesCountForPost(long postId) {
    var postDTO = postService.findById(postId).get();
    var post = modelMapper.map(postDTO, Post.class);
    return likeRepository.countByPost(post);
  }
}
