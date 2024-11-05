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
import ru.polovinko.socialnetwork.dto.FriendRequestCreateDTO;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.repository.FriendRequestRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.FriendRequestService;
import ru.polovinko.socialnetwork.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialnetworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class FriendRequestServiceImplTest extends ContainerEnvironment implements WithAssertions {
  @Autowired
  private FriendRequestService service;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private FriendRequestRepository friendRequestRepository;
  @Autowired
  private UserService userService;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void sendFriendRequestShouldSendRequestWhenUsersAreValid() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build()
    );
    var friend = userService.create(UserCreateDTO.builder()
      .username("friend")
      .email("friend@example.ru")
      .password("password")
      .build()
    );
    var create = service.create(FriendRequestCreateDTO.builder()
      .userId(user.getId())
      .friendId(friend.getId())
      .build());
    assertAll(
      () -> assertNotNull(create),
      () -> assertEquals(user.getId(), create.getUserId()),
      () -> assertEquals(friend.getId(), create.getFriendId()),
      () -> assertFalse(create.isAccepted())
    );
  }

  @Test
  void sendFriendRequestShouldThrowExceptionWhenRequestAlreadyExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build()
    );
    var friend = userService.create(UserCreateDTO.builder()
      .username("friend")
      .email("friend@exmaple.ru")
      .password("password")
      .build()
    );
    var create = FriendRequestCreateDTO.builder()
      .userId(user.getId())
      .friendId(friend.getId())
      .build();
    service.create(create);
    var exception = assertThrows(IllegalStateException.class, () ->
      service.create(create));
    assertTrue(exception.getMessage().contains("Friend request already sent"));
  }

  @Test
  void acceptFriendRequestShouldAcceptRequestWhenRequestExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build()
    );
    var friend = userService.create(UserCreateDTO.builder()
      .username("friend")
      .email("friend@example.ru")
      .password("password1")
      .build()
    );
    var create = service.create(FriendRequestCreateDTO.builder()
      .userId(user.getId())
      .friendId(friend.getId())
      .build());
    var acceptedRequest = service.update(create.getId());
    assertTrue(acceptedRequest.isAccepted());
  }

  @Test
  void rejectFriendRequestShouldDeleteRequestWhenRequestExists() {
    var user = userService.create(UserCreateDTO.builder()
      .username("user")
      .email("user@exmapler.ru")
      .password("password")
      .build()
    );
    var friend = userService.create(UserCreateDTO.builder()
      .username("friend")
      .email("friend@exmaple.ru")
      .password("password")
      .build()
    );
    var create = service.create(FriendRequestCreateDTO.builder()
      .userId(user.getId())
      .friendId(friend.getId())
      .build());
    service.delete(create.getId());
    assertTrue(friendRequestRepository.findById(create.getId()).isEmpty());
  }
}
