package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
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
  private final ModelMapper modelMapper;

  @Override
  public Page<PostDTO> search(PostSearchDTO dto, Pageable pageable) {
    var spec = new PostSpecification(dto);
    return postRepository.findAll(spec, pageable)
      .map(post -> modelMapper.map(post, PostDTO.class));
  }

  @Override
  public PostDTO create(PostCreateDTO dto) {
    var userSearchDTO = UserSearchDTO.builder()
      .userId(dto.getUserId())
      .build();
    var photoSearchDTO = PhotoSearchDTO.builder()
      .photoId(dto.getPhotoId())
      .build();
    var user = userService.search(userSearchDTO, Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(userDTO -> modelMapper.map(userDTO, User.class))
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
    var photo = photoService.search(photoSearchDTO, Pageable.unpaged())
      .getContent()
      .stream()
      .findFirst()
      .map(photoDTO -> modelMapper.map(photoDTO, Photo.class))
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", dto.getPhotoId())));
    var post = new Post(0L, dto.getContent(), user, new ArrayList<>(), new ArrayList<>(), photo);
    return modelMapper.map(postRepository.save(post), PostDTO.class);
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
    var photoSearchDTO = PhotoSearchDTO.builder()
      .photoId(dto.getPhotoId())
      .build();
      var photo = photoService.search(photoSearchDTO, Pageable.unpaged())
        .getContent()
        .stream()
        .findFirst()
        .map(photoDTO -> modelMapper.map(photoDTO, Photo.class))
        .orElseThrow(()-> new ObjectNotFoundException(String.format("Photo with ID %d not found", dto.getPhotoId())));
    post.setContent(dto.getContent());
    post.setPhoto(photo);
    return modelMapper.map(postRepository.save(post), PostDTO.class);
  }
}
