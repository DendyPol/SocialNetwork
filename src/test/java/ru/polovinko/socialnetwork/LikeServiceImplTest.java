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
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.LikeRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.LikeService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class LikeServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private LikeService service;
  @Autowired
  private LikeRepository likeRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    likeRepository.deleteAll();
    userRepository.deleteAll();
    postRepository.deleteAll();
  }

  @Test
  void likePostShouldCreateLikeWhenPostAndUserExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var like = service.likePost(post.getId(), user.getId());
    assertAll(
      () -> assertNotNull(like.getId()),
      () -> assertEquals(post.getId(), like.getPostId()),
      () -> assertEquals(user.getId(), like.getUserId()),
      () -> assertTrue(likeRepository.existsByPostAndUser(post, user))
    );
  }

  @Test
  void likePostShouldThrowExceptionWhenLikeAlreadyExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    likeRepository.save(Like.builder().post(post).user(user).build());
    var exception = assertThrows(IllegalStateException.class, () ->
      service.likePost(post.getId(), user.getId())
    );
    assertEquals("User has already liked this post", exception.getMessage());
  }

  @Test
  void unlikePostShouldDeleteLikeWhenLikeExists() {
    var post = postRepository.save(Post.builder().content("Test post").build());
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    likeRepository.save(Like.builder().post(post).user(user).build());
    service.unlikePost(post.getId(), user.getId());
    assertFalse(likeRepository.existsByPostAndUser(post, user));
  }

  @Test
  void unlikePostShouldThrowExceptionWhenLikeDoesNotExists() {
    var post = postRepository.save(Post.builder().content("New post").build());
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var exception = assertThrows(ObjectNotFoundException.class, () ->
      service.unlikePost(post.getId(), user.getId()));
    var expectedMessage = String.format("Like not found for the given Post(id=%d", post.getId());
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
    assertTrue(actualMessage.contains("User(id=" + user.getId()));
  }

  @Test
  void getLikesCountForPostShouldThrowExceptionWhenPostDoesNotExist() {
    var exception = assertThrows(ObjectNotFoundException.class, () ->
      service.getLikesCountForPost(999L));
    assertEquals("Post with ID 999 not found", exception.getMessage());
  }
}
