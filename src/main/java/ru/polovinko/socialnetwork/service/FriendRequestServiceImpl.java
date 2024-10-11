package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.FriendRequest;
import ru.polovinko.socialnetwork.repository.FriendRequestRepository;
import ru.polovinko.socialnetwork.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {
  private final FriendRequestRepository friendRequestRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public FriendRequestDTO sendFriendRequest(long userId, long friendId) {
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    var friend = userRepository.findById(friendId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", friendId)));
    if (friendRequestRepository.existsByUserAndFriend(user, friend)) {
      throw new IllegalStateException("Friend request already sent");
    }
    var friendRequest = FriendRequest.builder()
      .user(user)
      .friend(friend)
      .accepted(false)
      .build();
    friendRequestRepository.save(friendRequest);
    return modelMapper.map(friendRequest, FriendRequestDTO.class);
  }

  @Override
  public FriendRequestDTO acceptFriendRequest(long requestId) {
    var friendRequest = friendRequestRepository.findById(requestId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Friend request with ID %d not found", requestId)));
    friendRequest.setAccepted(true);
    var user = friendRequest.getUser();
    var friend = friendRequest.getFriend();
    user.getFriends().add(friend);
    friend.getFriends().add(user);
    userRepository.save(user);
    userRepository.save(friend);
    friendRequestRepository.save(friendRequest);
    return modelMapper.map(friendRequest, FriendRequestDTO.class);
  }

  @Override
  public void rejectFriendRequest(long requestId) {
    var friendRequest = friendRequestRepository.findById(requestId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Friend request with ID %d not found", requestId)));
    friendRequestRepository.delete(friendRequest);
  }

  @Override
  public List<FriendRequestDTO> getFriendRequestForUser(long userId) {
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    return friendRequestRepository.findByFriendAndAcceptedFalse(user).stream()
      .map(friendRequest -> modelMapper.map(friendRequest, FriendRequestDTO.class))
      .collect(Collectors.toList());
  }
}
