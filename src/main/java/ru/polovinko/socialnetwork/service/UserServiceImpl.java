package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.util.PasswordUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;


  @Override
  public List<UserDTO> findAll() {
    return userRepository.findAll().stream()
      .map(user -> modelMapper.map(user, UserDTO.class))
      .collect(Collectors.toList());
  }

  @Override
  public Optional<UserDTO> findById(long userId) {
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    return Optional.ofNullable(modelMapper.map(user, UserDTO.class));
  }

  @Override
  public UserDTO create(UserDTO dto) {
    if (userRepository.existsByUsername(dto.getUsername())) {
      throw new IllegalStateException(String.format("Username %s is already taken", dto.getUsername()));
    }
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalStateException(String.format("Email %s is already registered", dto.getEmail()));
    }
    var user = modelMapper.map(dto, User.class);
    user.setPassword(PasswordUtil.encodePassword(dto.getPassword()));
    return modelMapper.map(userRepository.save(user), UserDTO.class);
  }

  @Override
  public Optional<UserDTO> login(String username, String rawPassword) {
    var user = userRepository.findByUsername(username)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with username %s not found", username)));
    var encodedPassword = PasswordUtil.encodePassword(rawPassword);
    if (!user.getPassword().equals(encodedPassword)) {
      throw new IllegalStateException("Invalid username or password");
    }
    return Optional.ofNullable(modelMapper.map(user, UserDTO.class));
  }

  @Override
  public UserDTO update(long id, UserUpdateDTO dto) {
    var user = userRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", id)));
    Optional.ofNullable(dto.getUsername()).ifPresent(user::setUsername);
    Optional.ofNullable(dto.getPassword()).ifPresent(user::setPassword);
    Optional.ofNullable(dto.getEmail()).ifPresent(user::setEmail);
    return modelMapper.map(userRepository.save(user), UserDTO.class);
  }

  @Override
  public void deleteById(long userId) {
    userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    userRepository.deleteById(userId);
  }

  @Override
  public List<UserDTO> getUserFriend(long userId) {
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    return user.getFriends().stream()
      .map(friend -> modelMapper.map(friend, UserDTO.class))
      .collect(Collectors.toList());
  }
}
