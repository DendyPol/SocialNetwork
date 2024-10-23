package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.polovinko.socialnetwork.dto.CommentCreateDTO;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentSearchDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Comment;
import ru.polovinko.socialnetwork.repository.CommentRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.specification.CommentSpecification;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final ModelMapper modelMapper;

  @Override
  public Page<CommentDTO> search(CommentSearchDTO dto, Pageable pageable) {
    var spec = new CommentSpecification(dto);
    return commentRepository.findAll(spec, pageable)
      .map(comment -> modelMapper.map(comment, CommentDTO.class));
  }

  @Override
  public CommentDTO create(CommentCreateDTO dto) {
    var post = postRepository.findById(dto.getPostId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", dto.getPostId())));
    var comment = Comment.builder()
      .content(dto.getContent())
      .post(post)
      .build();
    return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
  }

  @Override
  public void deleteById(long id) {
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
