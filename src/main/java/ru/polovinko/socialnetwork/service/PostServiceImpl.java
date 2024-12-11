package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.mapper.CommonMapper;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.specification.PostSpecification;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;
  private final UserService userService;
  private final PhotoService photoService;
  private final CommonMapper commonMapper;

  @Override
  public Page<PostDTO> search(PostSearchDTO dto, Pageable pageable) {
    var spec = new PostSpecification(dto);
    return postRepository.findAll(spec, pageable)
      .map(commonMapper::toPostDTO);
  }

  @Override
  public PostDTO create(PostCreateDTO dto) {
    var user = userService.search(UserSearchDTO.builder()
        .userId(dto.getUserId())
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(commonMapper::toUser)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
    var photo = photoService.search(PhotoSearchDTO.builder()
        .photoId(dto.getPhotoId())
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(commonMapper::toPhoto)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", dto.getPhotoId())));
    var post = new Post(0L, dto.getContent(), user, new ArrayList<>(), new ArrayList<>(), photo);
    return commonMapper.toPostDTO(postRepository.save(post));
  }

  @Override
  public void delete(long id) {
    postRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
    postRepository.deleteById(id);
  }

  @Override
  public PostDTO update(PostUpdateDTO dto) {
    var post = postRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", dto.getId())));
    var photo = photoService.search(PhotoSearchDTO.builder()
        .photoId(dto.getPhotoId())
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(commonMapper::toPhoto)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", dto.getPhotoId())));
    commonMapper.updatePostFromDTO(dto, post);
    post.setPhoto(photo);
    return commonMapper.toPostDTO(postRepository.save(post));
  }
}
