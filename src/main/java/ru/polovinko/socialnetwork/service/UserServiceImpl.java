package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.model.User;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.specification.UserSpecification;
import ru.polovinko.socialnetwork.util.PasswordUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public Page<UserDTO> search(UserSearchDTO dto, Pageable pageable) {
    var spec = new UserSpecification(dto);
    return userRepository.findAll(spec, pageable)
      .map(user -> modelMapper.map(user, UserDTO.class));
  }

  @Override
  public Optional<UserDTO> findById(long userId) {
    var user = userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    return Optional.ofNullable(modelMapper.map(user, UserDTO.class));
  }

  @Override
  public UserDTO create(UserCreateDTO dto) {
    var user = modelMapper.map(dto, User.class);
    user.setPassword(PasswordUtil.encodePassword(dto.getPassword()));
    return modelMapper.map(userRepository.save(user), UserDTO.class);
  }

  @Override
  public UserDTO update(UserUpdateDTO dto) {
    var user = userRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getId())));
    Optional.ofNullable(dto.getUsername()).ifPresent(user::setUsername);
    Optional.ofNullable(dto.getPassword()).ifPresent(user::setPassword);
    Optional.ofNullable(dto.getEmail()).ifPresent(user::setEmail);
    return modelMapper.map(userRepository.save(user), UserDTO.class);
  }

  @Override
  public void delete(long userId) {
    userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    userRepository.deleteById(userId);
  }
}
