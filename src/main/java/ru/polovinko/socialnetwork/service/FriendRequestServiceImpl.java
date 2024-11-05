package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.FriendRequestCreateDTO;
import ru.polovinko.socialnetwork.dto.FriendRequestDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.FriendRequest;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.FriendRequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {
  private final FriendRequestRepository friendRequestRepository;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public FriendRequestDTO create(FriendRequestCreateDTO dto) {
    var userDTO = userService.findById(dto.getUserId()).get();
    var friendDTO = userService.findById(dto.getFriendId()).get();
    var user = modelMapper.map(userDTO, User.class);
    var friend = modelMapper.map(friendDTO, User.class);
    if (friendRequestRepository.existsByUserAndFriend(user, friend)) {
      throw new IllegalStateException("Friend request already sent");
    }
    var friendRequest = new FriendRequest();
    friendRequest.setUser(user);
    friendRequest.setFriend(friend);
    friendRequest.setAccepted(false);
    friendRequestRepository.save(friendRequest);
    return modelMapper.map(friendRequest, FriendRequestDTO.class);
  }

  @Override
  public FriendRequestDTO update(long id) {
    var friendRequest = friendRequestRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Friend request with ID %d not found", id)));
    friendRequest.setAccepted(true);
    var user = friendRequest.getUser();
    var friend = friendRequest.getFriend();
    userService.addFriend(user, friend);
    userService.addFriend(friend, user);
    friendRequestRepository.save(friendRequest);
    return modelMapper.map(friendRequest, FriendRequestDTO.class);
  }

  @Override
  public void delete(long id) {
    var friendRequest = friendRequestRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Friend request with ID %d not found", id)));
    friendRequestRepository.delete(friendRequest);
  }

  @Override
  public List<FriendRequestDTO> getFriendRequestForUser(long id) {
    var userDTO = userService.findById(id);
    var user = modelMapper.map(userDTO, User.class);
    return friendRequestRepository.findByFriendAndAcceptedFalse(user).stream()
      .map(friendRequest -> modelMapper.map(friendRequest, FriendRequestDTO.class))
      .collect(Collectors.toList());
  }
}
