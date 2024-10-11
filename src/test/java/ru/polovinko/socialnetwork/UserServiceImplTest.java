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
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  UserService service;
  @Autowired
  UserRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void createUserShouldCreateUserWhenDataIsValid() {
    var user = UserDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build();
    var createdUser = service.create(user);
    assertAll(
      () -> assertNotNull(createdUser),
      () -> assertNotNull(createdUser.getId()),
      () -> assertEquals(user.getUsername(), createdUser.getUsername()),
      () -> assertEquals(user.getEmail(), createdUser.getEmail())
    );
  }

  @Test
  void createUserShouldThrowExceptionWhenUsernameIsTaken() {
    var user = UserDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build();
    service.create(user);
    var duplicateUser = UserDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build();
    var exception = assertThrows(IllegalStateException.class, () -> service.create(duplicateUser));
    assertTrue(exception.getMessage().contains("Username testUser is already taken"));
  }

  @Test
  void updateUserShouldUpdateUserWhenDataIsValid() {
    var user = UserDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build();
    var createdUser = service.create(user);
    var updateUser = UserUpdateDTO.builder()
      .username("updatedUser")
      .email("updateuser@example.ru")
      .build();
    var updatedUser = service.update(createdUser.getId(), updateUser);
    assertAll(
      () -> assertEquals(updateUser.getUsername(), updatedUser.getUsername()),
      () -> assertEquals(updateUser.getEmail(), updatedUser.getEmail())
    );
  }

  @Test
  void deleteUserShouldRemoveUserWhenUserExist() {
    var user = UserDTO.builder()
      .username("testUser")
      .email("testuser@exmaple.ru")
      .password("password123")
      .build();
    var createdUser = service.create(user);
    service.deleteById(createdUser.getId());
    assertTrue(repository.findById(createdUser.getId()).isEmpty());
  }
}
