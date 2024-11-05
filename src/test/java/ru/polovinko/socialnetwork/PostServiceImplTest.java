package ru.polovinko.socialnetwork;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
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
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.PhotoService;
import ru.polovinko.socialnetwork.service.PostService;
import ru.polovinko.socialnetwork.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PostServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private PostService postService;
  @Autowired
  private PhotoService photoService;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void createPostShouldSucceed() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.com/photo.jpg")
      .userId(user.getId())
      .build());
    var post = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("post")
      .build());
    assertAll(
      () -> assertNotNull(post),
      () -> assertEquals(user.getId(), post.getUserId()),
      () -> assertEquals(photo.getId(), post.getPhotoId())
    );
  }

  @Test
  void findPostsWithPaginationShouldReturnFilteredPosts() {
    var user = userService.create(UserCreateDTO.builder()
      .username("username")
      .email("user@example.ru")
      .password("password")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var postOne = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("first post")
      .build());
    var postTwo = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("second post")
      .build());
    var searchDTO = PostSearchDTO.builder()
      .userId(user.getId())
      .build();
    Pageable pageable = PageRequest.of(0, 10);
    var resultPage = postService.search(searchDTO, pageable);
    assertAll(
      () -> assertEquals(2, resultPage.getTotalElements()),
      () -> assertTrue(resultPage.getContent().stream().anyMatch(p -> p.getContent().equals(postOne.getContent()))),
      () -> assertTrue(resultPage.getContent().stream().anyMatch(p -> p.getContent().equals(postTwo.getContent())))
    );
  }

  @Test
  void updatePostShouldModifyPost() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build()
    );
    var post = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("post")
      .build());
    var updatePost = PostUpdateDTO.builder()
      .id(post.getId())
      .content("Updated post")
      .build();
    var updatedPost = postService.update(updatePost);
    assertEquals(updatePost.getContent(), updatedPost.getContent());
  }

  @Test
  void deletePostShouldRemovePost() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build());
    var photo = photoService.create(PhotoCreateDTO.builder()
      .url("http://example.ru/photo.jpg")
      .userId(user.getId())
      .build());
    var createPost = postService.create(PostCreateDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("post")
      .build());
    postService.deleteById(createPost.getId());
    assertThrows(ObjectNotFoundException.class, () -> postService.findById(createPost.getId()).isPresent());
  }
}
