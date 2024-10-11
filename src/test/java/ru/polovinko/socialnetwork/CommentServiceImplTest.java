package ru.polovinko.socialnetwork;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.socialnetwork.config.ContainerEnvironment;
import ru.polovinko.socialnetwork.dto.CommentDTO;
import ru.polovinko.socialnetwork.dto.CommentUpdateDTO;
import ru.polovinko.socialnetwork.model.Comment;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.repository.CommentRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.service.CommentService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CommentServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private CommentService service;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void setUp() {
    commentRepository.deleteAll();
    postRepository.deleteAll();
  }

  @Test
  void findAllCommentsForPostShouldReturnCommentsWhenPostExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var commentOne = commentRepository.save(Comment.builder().content("Comment One").post(post).build());
    var commentTwo = commentRepository.save(Comment.builder().content("Comment Two").post(post).build());
    var comments = service.findAllCommentsForPost(post.getId());
    assertEquals(2, comments.size());
    assertEquals(commentOne.getContent(), comments.get(0).getContent());
    assertEquals(commentTwo.getContent(), comments.get(1).getContent());
  }

  @Test
  void createShouldCreateCommentWhenPostExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var comment = CommentDTO.builder().content("New comment").build();
    var createdComment = service.create(post.getId(), comment);
    assertNotNull(createdComment.getId());
    assertEquals(comment.getContent(), createdComment.getContent());
    assertTrue(commentRepository.existsById(createdComment.getId()));
  }

  @Test
  void deleteByIdShouldDeleteCommentWhenCommentExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var comment = commentRepository.save(Comment.builder().content("New comment").post(post).build());
    service.deleteById(comment.getId());
    assertFalse(commentRepository.existsById(comment.getId()));
  }

  @Test
  void updateShouldUpdateCommentWhenCommentExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var comment = commentRepository.save(Comment.builder().content("New comment").post(post).build());
    var updateComment = CommentUpdateDTO.builder().content("Update comment").build();
    var updatedComment = service.update(comment.getId(), updateComment);
    assertEquals(updateComment.getContent(), updatedComment.getContent());
  }
}
