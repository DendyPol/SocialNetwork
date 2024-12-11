package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.EntityExistException;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.mapper.CommonMapper;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.repository.LikeRepository;
import ru.polovinko.socialnetwork.specification.LikeSpecification;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
  private final LikeRepository likeRepository;
  private final PostService postService;
  private final UserService userService;
  private final CommonMapper commonMapper;

  @Override
  public LikeDTO create(LikeCreateDTO dto) {
    var user = userService.search(UserSearchDTO.builder()
        .userId(dto.getUserId())
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(commonMapper::toUser)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
    var post = postService.search(PostSearchDTO.builder()
        .postId(dto.getPostId())
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(commonMapper::toPost)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", dto.getPostId())));
    if (likeRepository.existsByPostAndUser(post, user)) {
      throw new EntityExistException("User has already liked this post");
    }
    var like = new Like(0L, post, user);
    return commonMapper.toLikeDTO(likeRepository.save(like));
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
      .map(commonMapper::toLikeDTO);
  }
}
