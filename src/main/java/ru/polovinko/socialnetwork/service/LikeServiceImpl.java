package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.EntityExistException;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.LikeRepository;
import ru.polovinko.socialnetwork.specification.LikeSpecification;

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
    var userSearchDTO = UserSearchDTO.builder()
      .userId(dto.getUserId())
      .build();
    var postSearchDTO = PostSearchDTO.builder()
      .postId(dto.getPostId())
      .build();
    var user = userService.search(userSearchDTO, Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(userDTO -> modelMapper.map(userDTO, User.class))
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
    var post = postService.search(postSearchDTO, Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(postDTO -> modelMapper.map(postDTO, Post.class))
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", dto.getPostId())));
    if (likeRepository.existsByPostAndUser(post, user)) {
      throw new EntityExistException("User has already liked this post");
    }
    var like = new Like(0L, post, user);
    return modelMapper.map(likeRepository.save(like), LikeDTO.class);
  }

  @Override
  public void delete(long id) {
    var like = likeRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Like with ID %d not found", id)));
    likeRepository.delete(like);
  }

  @Override
  public Page<LikeDTO> search(LikeSearchDTO dto, Pageable pageable) {
    var spec = new LikeSpecification(dto);
    return likeRepository.findAll(spec, pageable)
      .map(like -> modelMapper.map(like, LikeDTO.class));
  }
}
