package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.PostCreateDTO;
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostSearchDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.specification.PostSpecification;

import java.util.Optional;

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
  public Optional<PostDTO> findById(long id) {
    var post = postRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
    return Optional.ofNullable(modelMapper.map(post, PostDTO.class));
  }

  @Override
  public PostDTO create(PostCreateDTO dto) {
    var userDTO = userService.findById(dto.getUserId()).get();
    var photoDTO = photoService.photoById(dto.getPhotoId()).get();
    var user = modelMapper.map(userDTO, User.class);
    var photo = modelMapper.map(photoDTO, Photo.class);
    var post = new Post();
    post.setUser(user);
    post.setPhoto(photo);
    post.setContent(dto.getContent());
    return modelMapper.map(postRepository.save(post), PostDTO.class);
  }

  @Override
  public void deleteById(long id) {
    postRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", id)));
    postRepository.deleteById(id);
  }

  @Override
  public PostDTO update(PostUpdateDTO dto) {
    var post = postRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", dto.getId())));
    Optional.ofNullable(dto.getContent()).ifPresent(post::setContent);
    Optional.ofNullable(dto.getPhotoId()).ifPresent(photoId -> {
      var photoDTO = photoService.photoById(dto.getPhotoId()).get();
      var photo = modelMapper.map(photoDTO, Photo.class);
      post.setPhoto(photo);
    });
    return modelMapper.map(postRepository.save(post), PostDTO.class);
  }
}
