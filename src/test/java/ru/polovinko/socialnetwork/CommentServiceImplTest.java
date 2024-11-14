package ru.polovinko.socialnetwork;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.socialnetwork.config.ContainerEnvironment;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.repository.CommentRepository;
import ru.polovinko.socialnetwork.service.CommentService;
import ru.polovinko.socialnetwork.service.PhotoService;
import ru.polovinko.socialnetwork.service.PostService;
import ru.polovinko.socialnetwork.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CommentServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private CommentService commentService;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private PhotoService photoService;
  @Autowired
  private PostService postService;

  @Test
  void findAllCommentsForPostShouldReturnCommentsWhenPostExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("username")
      .password("password")
      .email("email@email.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("Test post")
      .build());
    var commentOne = commentService.create(CommentCreateDTO.builder()
      .userId(user.getId())
      .postId(post.getId())
      .content("Comment One")
      .build());
    var commentTwo = commentService.create(CommentCreateDTO.builder()
      .userId(user.getId())
      .postId(post.getId())
      .content("Comment Two")
      .build());
    var searchDTO = CommentSearchDTO.builder()
      .postId(post.getId())
      .build();
    Pageable pageable = PageRequest.of(0, 10);
    var commentsPage = commentService.search(searchDTO, pageable);
    assertAll(
      () -> assertEquals(2, commentsPage.getTotalElements()),
      () -> assertTrue(commentsPage.getContent().stream().anyMatch(c -> c.getContent().equals(commentOne.getContent()))),
      () -> assertTrue(commentsPage.getContent().stream().anyMatch(c -> c.getContent().equals(commentTwo.getContent())))
    );
  }

  @Test
  void createShouldCreateCommentWhenPostExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("username")
      .password("password")
      .email("email@email.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("Test post")
      .build());
    var comment = CommentCreateDTO.builder()
      .userId(user.getId())
      .content("New comment")
      .postId(post.getId())
      .build();
    var createdComment = commentService.create(comment);
    assertNotNull(createdComment.getId());
    assertEquals(comment.getContent(), createdComment.getContent());
    assertTrue(commentRepository.existsById(createdComment.getId()));
  }

  @Test
  void deleteByIdShouldDeleteCommentWhenCommentExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("username")
      .password("password")
      .email("email@email.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("Test post")
      .build());
    var comment = commentService.create(CommentCreateDTO.builder()
      .userId(user.getId())
      .content("New comment")
      .postId(post.getId())
      .build());
    commentService.delete(comment.getId());
    assertFalse(commentRepository.existsById(comment.getId()));
  }

  @Test
  void updateShouldUpdateCommentWhenCommentExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("username")
      .password("password")
      .email("email@email.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("Test post")
      .build());
    var comment = commentService.create(CommentCreateDTO.builder()
      .userId(user.getId())
      .content("New comment")
      .postId(post.getId())
      .build());
    var updateComment = CommentUpdateDTO.builder()
      .id(comment.getId())
      .content("Update comment")
      .build();
    var updatedComment = commentService.update(updateComment);
    assertEquals(updateComment.getContent(), updatedComment.getContent());
  }
}
