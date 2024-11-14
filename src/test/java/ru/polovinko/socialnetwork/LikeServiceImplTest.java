package ru.polovinko.socialnetwork;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.socialnetwork.config.ContainerEnvironment;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.AlreadyExistException;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.service.LikeService;
import ru.polovinko.socialnetwork.service.PhotoService;
import ru.polovinko.socialnetwork.service.PostService;
import ru.polovinko.socialnetwork.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class LikeServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private LikeService likeService;
  @Autowired
  private PostService postService;
  @Autowired
  private UserService userService;
  @Autowired
  private PhotoService photoService;

  @Test
  void likePostShouldCreateLikeWhenPostAndUserExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("razDvaTri")
      .email("user@example.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .content("Test post")
      .userId(user.getId())
      .photoId(photo.getId())
      .build());
    var create = likeService.create(LikeCreateDTO.builder()
      .postId(post.getId())
      .userId(user.getId())
      .build());
    assertAll(
      () -> assertNotNull(create.getId()),
      () -> assertEquals(post.getId(), create.getPostId()),
      () -> assertEquals(user.getId(), create.getUserId())
    );
  }

  @Test
  void likePostShouldThrowExceptionWhenLikeAlreadyExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("razDvaTri")
      .email("user@example.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .content("Test post")
      .userId(user.getId())
      .photoId(photo.getId())
      .build());
    var create = LikeCreateDTO.builder()
      .postId(post.getId())
      .userId(user.getId())
      .build();
    likeService.create(create);
    var exception = assertThrows(AlreadyExistException.class, () ->
      likeService.create(create)
    );
    assertEquals("User has already liked this post", exception.getMessage());
  }

  @Test
  void unlikePostShouldDeleteLikeWhenLikeExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("razDvaTri")
      .email("user@example.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .content("Test post")
      .userId(user.getId())
      .photoId(photo.getId())
      .build());
    var like = likeService.create(LikeCreateDTO.builder()
      .userId(user.getId())
      .postId(post.getId())
      .build());
    likeService.delete(like.getId());
    assertThrows(ObjectNotFoundException.class, () -> likeService.delete(like.getId()));
  }

  @Test
  void unlikePostShouldThrowExceptionWhenLikeDoesNotExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("razDvaTri")
      .email("user@example.ru")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .content("Test post")
      .userId(user.getId())
      .photoId(photo.getId())
      .build());
    var exception = assertThrows(ObjectNotFoundException.class, () ->
      likeService.delete(post.getId()));
    var expectedMessage = String.format("Like with ID %d not found", post.getId());
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void searchLikeWithPaginationShouldReturnLikeWhenLikeExist() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .password("password")
      .email("email@example.ru")
      .build()
    );
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build()
    );
    var post = postService.create(PostCreateDTO.builder()
      .content("Test post")
      .userId(user.getId())
      .photoId(photo.getId())
      .build()
    );
    likeService.create(LikeCreateDTO.builder()
      .postId(post.getId())
      .userId(user.getId())
      .build()
    );
    Pageable pageable = PageRequest.of(0, 10);
    var dto = LikeSearchDTO.builder()
      .postId(post.getId())
      .build();
    Page<LikeDTO> result = likeService.search(dto, pageable);
    assertAll(
      () -> assertEquals(1, result.getTotalElements()),
      () -> assertEquals(1, result.getContent().size()),
      () -> assertEquals(user.getId(), result.getContent().get(0).getUserId()),
      () -> assertEquals(post.getId(), result.getContent().get(0).getPostId())
    );
  }
}
