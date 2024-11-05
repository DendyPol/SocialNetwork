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
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
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
  UserService userService;
  @Autowired
  UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void createUserShouldCreateUserWhenDataIsValid() {
    var user = userService.create(UserCreateDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build());
    var expectedUser = UserDTO.builder()
      .id(user.getId())
      .username(user.getUsername())
      .email(user.getEmail())
      .password(user.getPassword())
      .build();
    assertAll(
      () -> assertNotNull(expectedUser),
      () -> assertNotNull(expectedUser.getId()),
      () -> assertEquals(user, expectedUser),
      () -> assertEquals(user.hashCode(), expectedUser.hashCode())
    );
  }

  @Test
  void createUserShouldThrowExceptionWhenUsernameIsTaken() {
    var user = userService.create(UserCreateDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build());
    var duplicateUser = UserCreateDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build();
    var exception = assertThrows(IllegalStateException.class, () -> userService.create(duplicateUser));
    assertTrue(exception.getMessage().contains("Username testUser is already taken"));
  }

  @Test
  void updateUserShouldUpdateUserWhenDataIsValid() {
    var user = userService.create(UserCreateDTO.builder()
      .username("testUser")
      .email("testuser@example.ru")
      .password("password123")
      .build());
    var updateUser = UserUpdateDTO.builder()
      .id(user.getId())
      .username("updatedUser")
      .email("updateuser@example.ru")
      .password("password456")
      .build();
    var updatedUser = userService.update(updateUser);
    assertAll(
      () -> assertEquals(updateUser.getUsername(), updatedUser.getUsername()),
      () -> assertEquals(updateUser.getEmail(), updatedUser.getEmail())
    );
  }

  @Test
  void deleteUserShouldRemoveUserWhenUserExist() {
    var user = userService.create(UserCreateDTO.builder()
      .username("testUser")
      .email("testuser@exmaple.ru")
      .password("password123")
      .build());
    userService.deleteById(user.getId());
    assertTrue(userRepository.findById(user.getId()).isEmpty());
  }
}
