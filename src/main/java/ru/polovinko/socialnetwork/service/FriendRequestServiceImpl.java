package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.EntityExistException;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.FriendRequest;
import ru.polovinko.socialnetwork.model.FriendshipStatus;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.FriendRequestRepository;
import ru.polovinko.socialnetwork.specification.FriendRequestSpecification;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {
  private final FriendRequestRepository friendRequestRepository;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Override
  public FriendRequestDTO create(FriendRequestCreateDTO dto) {
    Function<Long, User> findUsedById = userId -> {
      var userSearchDTO = UserSearchDTO.builder()
        .userId(userId)
        .build();
      return userService.search(userSearchDTO, Pageable.unpaged())
        .getContent()
        .stream()
        .findFirst()
        .map(userDTO -> modelMapper.map(userDTO, User.class))
        .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    };
    var sender = findUsedById.apply(dto.getSenderId());
    var recipient = findUsedById.apply(dto.getRecipientId());
    if (friendRequestRepository.existsBySenderAndRecipient(sender, recipient)) {
      throw new EntityExistException("Friend request already sent");
    }
    var friendRequest = new FriendRequest(0L, sender, recipient, FriendshipStatus.PENDING);
    friendRequestRepository.save(friendRequest);
    return modelMapper.map(friendRequest, FriendRequestDTO.class);
  }

  @Override
  public FriendRequestDTO update(FriendRequestUpdateDTO dto) {
    var friendRequest = friendRequestRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Friend request with ID %d not found", dto.getId())));
    if (dto.getStatus() == FriendshipStatus.PENDING) {
      throw new EntityExistException("Cannot update a request to PENDING status!");
    }
    friendRequest.setStatus(dto.getStatus());
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
  public Page<FriendRequestDTO> search(FriendRequestSearchDTO dto, Pageable pageable) {
    var spec = new FriendRequestSpecification(dto);
    return friendRequestRepository.findAll(spec, pageable)
      .map(friendRequest -> modelMapper.map(friendRequest, FriendRequestDTO.class));
  }
}
