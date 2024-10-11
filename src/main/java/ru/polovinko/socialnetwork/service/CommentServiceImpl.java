package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Comment;
import ru.polovinko.socialnetwork.repository.CommentRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<CommentDTO> findAllCommentsForPost(long postId) {
    var post = postRepository.findById(postId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", postId)));
    return commentRepository.findByPost(post).stream()
      .map(comment -> modelMapper.map(comment, CommentDTO.class))
      .collect(Collectors.toList());
  }

  @Override
  public CommentDTO create(long postId, CommentDTO dto) {
    var post = postRepository.findById(postId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Post with ID %d not found", postId)));
    var comment = modelMapper.map(dto, Comment.class);
    comment.setPost(post);
    return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
  }

  @Override
  public void deleteById(long id) {
    commentRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Comment with ID %d not found", id)));
    commentRepository.deleteById(id);
  }

  @Override
  public CommentDTO update(long id, CommentUpdateDTO dto) {
    var comment = commentRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Comment with ID %d not found", id)));
    Optional.ofNullable(dto.getContent()).ifPresent(comment::setContent);
    return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
  }
}
