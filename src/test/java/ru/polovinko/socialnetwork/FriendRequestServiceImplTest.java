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
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.FriendRequestRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.service.FriendRequestService;

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

  @BeforeEach
  void setUp() {
    friendRequestRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void sendFriendRequestShouldSendRequestWhenUsersAreValid() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build()
    );
    var friend = userRepository.save(User.builder()
      .username("friend")
      .email("friend@example.ru")
      .password("password")
      .build()
    );
    var request = service.sendFriendRequest(user.getId(), friend.getId());
    assertAll(
      () -> assertNotNull(request),
      () -> assertEquals(user.getId(), request.getUserId()),
      () -> assertEquals(friend.getId(), request.getFriendId()),
      () -> assertFalse(request.isAccepted())
    );
  }

  @Test
  void sendFriendRequestShouldThrowExceptionWhenRequestAlreadyExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build()
    );
    var friend = userRepository.save(User.builder()
      .username("friend")
      .email("friend@exmaple.ru")
      .password("password")
      .build()
    );
    service.sendFriendRequest(user.getId(), friend.getId());
    var exception = assertThrows(IllegalStateException.class, () ->
      service.sendFriendRequest(user.getId(), friend.getId()));
    assertTrue(exception.getMessage().contains("Friend request already sent"));
  }

  @Test
  void acceptFriendRequestShouldAcceptRequestWhenRequestExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@example.ru")
      .password("password")
      .build()
    );
    var friend = userRepository.save(User.builder()
      .username("friend")
      .email("friend@example.ru")
      .password("password1")
      .build()
    );
    var request = service.sendFriendRequest(user.getId(), friend.getId());
    var acceptedRequest = service.acceptFriendRequest(request.getId());
    assertTrue(acceptedRequest.isAccepted());
  }

  @Test
  void rejectFriendRequestShouldDeleteRequestWhenRequestExists() {
    var user = userRepository.save(User.builder()
      .username("user")
      .email("user@exmapler.ru")
      .password("password")
      .build()
    );
    var friend = userRepository.save(User.builder()
      .username("friend")
      .email("friend@exmaple.ru")
      .password("password")
      .build()
    );
    var request = service.sendFriendRequest(user.getId(), friend.getId());
    service.rejectFriendRequest(request.getId());
    assertTrue(friendRequestRepository.findById(request.getId()).isEmpty());
  }
}
