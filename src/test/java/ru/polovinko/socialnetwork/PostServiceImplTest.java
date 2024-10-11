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
import ru.polovinko.socialnetwork.dto.PostDTO;
import ru.polovinko.socialnetwork.dto.PostUpdateDTO;
import ru.polovinko.socialnetwork.model.Photo;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.PhotoRepository;
import ru.polovinko.socialnetwork.repository.PostRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.PostService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PostServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private PostService service;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PhotoRepository photoRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    postRepository.deleteAll();
    photoRepository.deleteAll();
  }

  @Test
  void createPostShouldSucceed() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var photo = photoRepository.save(Photo.builder()
      .url("http://example.com/photo.jpg")
      .user(user)
      .build()
    );
    var post = PostDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("post")
      .build();
    var createdPost = service.create(post);
    assertAll(
      () -> assertNotNull(createdPost),
      () -> assertEquals(post.getContent(), createdPost.getContent()),
      () -> assertEquals(user.getId(), createdPost.getUserId()),
      () -> assertEquals(photo.getId(), createdPost.getPhotoId())
    );
  }

  @Test
  void findAllPostsShouldReturnListOfPosts() {
    var user = userRepository.save(User.builder()
      .username("username")
      .email("user@example.ru")
      .build()
    );
    var photo = photoRepository.save(Photo.builder()
      .url("http://example.ru/photo.jpg")
      .user(user)
      .build()
    );
    service.create(PostDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("first post")
      .build()
    );
    service.create(PostDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("second post")
      .build()
    );
    var posts = service.findAll();
    assertEquals(2, posts.size());
  }

  @Test
  void updatePostShouldModifyPost() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var photo = photoRepository.save(Photo.builder()
      .url("http://example.ru/photo.jpg")
      .user(user)
      .build()
    );
    var post = PostDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("post")
      .build();
    var createdPost = service.create(post);
    var updatePost = PostUpdateDTO.builder()
      .content("Updated post")
      .build();
    var updatedPost = service.update(createdPost.getId(), updatePost);
    assertEquals(updatePost.getContent(), updatedPost.getContent());
  }

  @Test
  void deletePostShouldRemovePost() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .build()
    );
    var photo = photoRepository.save(Photo.builder()
      .url("http://example.ru/photo.jpg")
      .user(user)
      .build()
    );
    var post = PostDTO.builder()
      .userId(user.getId())
      .photoId(photo.getId())
      .content("post")
      .build();
    var createdPost = service.create(post);
    service.deleteById(createdPost.getId());
    assertFalse(postRepository.findById(createdPost.getId()).isPresent());
  }
}
