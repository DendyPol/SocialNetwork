package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.mapper.CommonMapper;
import ru.polovinko.socialnetwork.model.Comment;
import ru.polovinko.socialnetwork.repository.CommentRepository;
import ru.polovinko.socialnetwork.specification.CommentSpecification;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  private final PostService postService;
  private final UserService userService;
  private final CommonMapper commonMapper;

  @Override
  public Page<CommentDTO> search(CommentSearchDTO dto, Pageable pageable) {
    var spec = new CommentSpecification(dto);
    return commentRepository.findAll(spec, pageable)
      .map(commonMapper::toCommentDTO);
  }

  @Override
  public CommentDTO create(CommentCreateDTO dto) {
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
    var comment = new Comment(0L, dto.getContent(), post, user);
    return commonMapper.toCommentDTO(commentRepository.save(comment));
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
    commonMapper.updateCommentFromDTO(dto, comment);
    return commonMapper.toCommentDTO(commentRepository.save(comment));
  }
}
