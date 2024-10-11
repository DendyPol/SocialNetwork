package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PhotoRepository photoRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<PostDTO> findAll() {
    return postRepository.findAll().stream()
      .map(post -> modelMapper.map(post, PostDTO.class))
      .collect(Collectors.toList());
  }

  @Override
  public Optional<PostDTO> findById(long id) {
    var post = postRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
    return Optional.ofNullable(modelMapper.map(post, PostDTO.class));
  }

  @Override
  public PostDTO create(PostDTO dto) {
    var user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getUserId())));
    var photo = Optional.ofNullable(dto.getPhotoId())
      .flatMap(photoRepository::findById)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", dto.getPhotoId())));
    var post = modelMapper.map(dto, Post.class);
    post.setUser(user);
    post.setPhoto(photo);
    return modelMapper.map(postRepository.save(post), PostDTO.class);
  }

  @Override
  public void deleteById(long id) {
    postRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
    postRepository.deleteById(id);
  }

  @Override
  public PostDTO update(long id, PostUpdateDTO dto) {
    var post = postRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
    Optional.ofNullable(dto.getContent()).ifPresent(post::setContent);
    Optional.ofNullable(dto.getPhotoId()).ifPresent(photoId -> {
      var photo = photoRepository.findById(photoId)
        .orElseThrow(() -> new ObjectNotFoundException(String.format("Photo with ID %d not found", photoId)));
      post.setPhoto(photo);
    });
    return modelMapper.map(postRepository.save(post), PostDTO.class);
  }
}
