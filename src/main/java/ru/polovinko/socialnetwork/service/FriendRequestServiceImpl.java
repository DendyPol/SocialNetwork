package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.exception.EntityExistException;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.mapper.CommonMapper;
import ru.polovinko.socialnetwork.model.FriendRequest;
import ru.polovinko.socialnetwork.model.FriendshipStatus;
import ru.polovinko.socialnetwork.repository.FriendRequestRepository;
import ru.polovinko.socialnetwork.specification.FriendRequestSpecification;
import ru.polovinko.socialnetwork.validation.RequestStatusValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {
  private final FriendRequestRepository friendRequestRepository;
  private final UserService userService;
  private final CommonMapper commonMapper;

  @Override
  public FriendRequestDTO create(FriendRequestCreateDTO dto) {
    var users = userService.search(UserSearchDTO.builder()
        .userIds(List.of(dto.getSenderId(), dto.getRecipientId()))
        .build(), Pageable.unpaged())
      .getContent()
      .stream()
      .map(commonMapper::toUser)
      .toList();
    var sender = users.stream()
      .filter(user -> user.getId() == dto.getSenderId())
      .findFirst()
      .orElseThrow(() -> new ObjectNotFoundException("Sender not found"));
    var recipient = users.stream()
      .filter(user -> user.getId() == dto.getRecipientId())
      .findFirst()
      .orElseThrow(() -> new ObjectNotFoundException("Recipient not found"));
    var friendRequest = new FriendRequest(0L, sender, recipient, FriendshipStatus.PENDING);
    return commonMapper.toFriendRequestDTO(friendRequestRepository.save(friendRequest));
  }

  @Override
  public FriendRequestDTO update(FriendRequestUpdateDTO dto) {
    var friendRequest = friendRequestRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("Friend request with ID %d not found", dto.getId())));
    RequestStatusValidator.validateStatus(friendRequest.getRequestStatus(), dto.getRequestStatus());
    friendRequest.setRequestStatus(dto.getRequestStatus());
    commonMapper.updateFriendRequestFromDTO(dto, friendRequest);
    return commonMapper.toFriendRequestDTO(friendRequestRepository.save(friendRequest));
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
      .map(commonMapper::toFriendRequestDTO);
  }
}
