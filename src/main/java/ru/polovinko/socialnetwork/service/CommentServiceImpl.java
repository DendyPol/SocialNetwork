package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Comment;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.CommentRepository;
import ru.polovinko.socialnetwork.specification.CommentSpecification;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  private final PostService postService;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public Page<CommentDTO> search(CommentSearchDTO dto, Pageable pageable) {
    var spec = new CommentSpecification(dto);
    return commentRepository.findAll(spec, pageable)
      .map(comment -> modelMapper.map(comment, CommentDTO.class));
  }

  @Override
  public CommentDTO create(CommentCreateDTO dto) {
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
    var comment = new Comment(0L, dto.getContent(), post, user);
    return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
  }

  @Override
  public void delete(long id) {
    commentRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Comment with ID %d not found", id)));
    commentRepository.deleteById(id);
  }

  @Override
  public CommentDTO update(CommentUpdateDTO dto) {
    var comment = commentRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Comment with ID %d not found", dto.getId())));
    Optional.ofNullable(dto.getContent()).ifPresent(comment::setContent);
    return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
  }
}
