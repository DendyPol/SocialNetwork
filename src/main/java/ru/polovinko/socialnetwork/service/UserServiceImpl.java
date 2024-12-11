package ru.polovinko.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.mapper.CommonMapper;
import ru.polovinko.socialnetwork.repository.UserRepository;
import ru.polovinko.socialnetwork.specification.UserSpecification;
import ru.polovinko.socialnetwork.util.PasswordUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final CommonMapper commonMapper;

  @Override
  public Page<UserDTO> search(UserSearchDTO dto, Pageable pageable) {
    var spec = new UserSpecification(dto);
    return userRepository.findAll(spec, pageable)
      .map(commonMapper::toUserDTO);
  }

  @Override
  public UserDTO create(UserCreateDTO dto) {
    var user = commonMapper.toCreateUser(dto);
    user.setPassword(PasswordUtil.encodePassword(dto.getPassword()));
    return commonMapper.toUserDTO(userRepository.save(user));
  }

  @Override
  public UserDTO update(UserUpdateDTO dto) {
    var user = userRepository.findById(dto.getId())
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", dto.getId())));
    commonMapper.updateUserFromDTO(dto, user);
    return commonMapper.toUserDTO(userRepository.save(user));
  }

  @Override
  public void delete(long userId) {
    userRepository.findById(userId)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", userId)));
    userRepository.deleteById(userId);
  }
}
