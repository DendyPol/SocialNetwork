package ru.polovinko.socialnetwork.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonMapperTest {
  @Autowired
  private CommonMapper commonMapperTest;

  @Test
  void toUserDTO() {
    var user = new User(1L, "username", "password", "email@email.ru",
      List.of(), List.of(), List.of(), List.of());
    var userDTO = commonMapperTest.toUserDTO(user);
    assertAll(
      () -> assertEquals(userDTO.getId(), user.getId()),
      () -> assertEquals(userDTO.getUsername(), user.getUsername()),
      () -> assertEquals(userDTO.getPassword(), user.getPassword()),
      () -> assertEquals(userDTO.getEmail(), user.getEmail())
    );
  }

  @Test
  void toUser() {
    var userDTO = UserDTO.builder()
      .id(1L)
      .username("username")
      .password("password")
      .email("email@email.ru")
      .build();
    var user = commonMapperTest.toUser(userDTO);
    assertAll(
      () -> assertEquals(user.getId(), userDTO.getId()),
      () -> assertEquals(user.getUsername(), userDTO.getUsername()),
      () -> assertEquals(user.getPassword(), userDTO.getPassword()),
      () -> assertEquals(user.getEmail(), userDTO.getEmail())
    );
  }

  @Test
  void toCreateUser() {
    var createUserDTO = UserCreateDTO.builder()
      .username("username")
      .password("password")
      .email("email")
      .build();
    var expectedUser = commonMapperTest.toCreateUser(createUserDTO);
    assertAll(
      () -> assertEquals(createUserDTO.getUsername(), expectedUser.getUsername()),
      () -> assertEquals(createUserDTO.getPassword(), expectedUser.getPassword()),
      () -> assertEquals(createUserDTO.getEmail(), expectedUser.getEmail())
    );
  }

  @Test
  void updateUserFromDTO() {
    var user = new User(1L, "username", "password", "email@email.ru",
      List.of(), List.of(), List.of(), List.of());
    var dto = UserUpdateDTO.builder()
      .username("newUsername")
      .password("newPassword")
      .build();
    commonMapperTest.updateUserFromDTO(dto, user);
    assertAll(
      () -> assertEquals(user.getUsername(), dto.getUsername()),
      () -> assertEquals(user.getPassword(), dto.getPassword()),
      () -> assertNotEquals(user.getEmail(), dto.getEmail())
    );
  }

  @Test
  void toPostDTO() {
    var post = new Post(1L, "content", null, List.of(), List.of(), null);
    var postDTO = commonMapperTest.toPostDTO(post);
    assertAll(
      () -> assertEquals(post.getId(), postDTO.getId()),
      () -> assertEquals(post.getContent(), postDTO.getContent())
    );
  }

  @Test
  void toPost() {
    var postDTO = PostDTO.builder()
      .id(1L)
      .content("content")
      .build();
    var post = commonMapperTest.toPost(postDTO);
    assertAll(
      () -> assertEquals(postDTO.getId(), post.getId()),
      () -> assertEquals(postDTO.getContent(), post.getContent())
    );
  }

  @Test
  void toCreatePost() {
    var postCreateDTO = PostCreateDTO.builder()
      .content("content")
      .build();
    var expectedPost = commonMapperTest.toCreatePost(postCreateDTO);
    assertEquals(postCreateDTO.getContent(), expectedPost.getContent());

  }

  @Test
  void updatePostFromDTO() {
    var post = new Post(1L, "content", null, List.of(), List.of(), null);
    var dto = PostUpdateDTO.builder()
      .content("new Content")
      .build();
    commonMapperTest.updatePostFromDTO(dto, post);
    assertEquals(post.getContent(), dto.getContent());
  }

  @Test
  void toPhotoDTO() {
    var photo = new Photo(1L, "someUrl", null);
    var photoDTO = commonMapperTest.toPhotoDTO(photo);
    assertEquals(photoDTO.getUrl(), photo.getUrl());
  }

  @Test
  void toPhoto() {
    var photoDTO = PhotoDTO.builder()
      .id(1L)
      .url("someUrl")
      .build();
    var photo = commonMapperTest.toPhoto(photoDTO);
    assertEquals(photo.getUrl(), photoDTO.getUrl());
  }

  @Test
  void toCreatePhoto() {
    var photoCreateDTO = PhotoCreateDTO.builder()
      .url("someUrl")
      .build();
    var expectedPhoto = commonMapperTest.toCreatePhoto(photoCreateDTO);
    assertEquals(photoCreateDTO.getUrl(), expectedPhoto.getUrl());
  }

  @Test
  void toLikeDTO() {
    var like = new Like(1L, null, null);
    var likeDTO = commonMapperTest.toLikeDTO(like);
    assertEquals(like.getId(), likeDTO.getId());
  }

  @Test
  void toFriendRequestDTO() {
    var friendRequest = new FriendRequest(1L, null, null, FriendshipStatus.PENDING);
    var friendRequestDTO = commonMapperTest.toFriendRequestDTO(friendRequest);
    assertAll(
      () -> assertEquals(friendRequest.getId(), friendRequestDTO.getId()),
      () -> assertEquals(friendRequest.getRequestStatus(), friendRequestDTO.getRequestStatus())
    );
  }

  @Test
  void updateFriendRequestFromDTO() {
    var friendRequest = new FriendRequest(1L, null, null, FriendshipStatus.PENDING);
    var dto = FriendRequestUpdateDTO.builder()
      .requestStatus(FriendshipStatus.ACCEPTED)
      .build();
    commonMapperTest.updateFriendRequestFromDTO(dto, friendRequest);
    assertEquals(friendRequest.getRequestStatus(), dto.getRequestStatus());
  }

  @Test
  void toCommentDTO() {
    var comment = new Comment(1L, "comment", null, null);
    var commentDTO = commonMapperTest.toCommentDTO(comment);
    assertAll(
      () -> assertEquals(comment.getId(), commentDTO.getId()),
      () -> assertEquals(comment.getContent(), commentDTO.getContent())
    );
  }

  @Test
  void updateCommentFromDTO() {
    var comment = new Comment(1L, "comment", null, null);
    var dto = CommentUpdateDTO.builder()
      .content("new comment")
      .build();
    commonMapperTest.updateCommentFromDTO(dto, comment);
    assertEquals(comment.getContent(), dto.getContent());
  }
}
