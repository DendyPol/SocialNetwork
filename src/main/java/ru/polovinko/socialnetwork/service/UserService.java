package ru.polovinko.socialnetwork.service;

import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
  List<UserDTO> findAll();

  Optional<UserDTO> findById(long id);

  UserDTO create(UserDTO userDTO);

  void deleteById(long id);

  List<UserDTO> getUserFriend(long userId);

  Optional<UserDTO> login(String username, String rawPassword);

  UserDTO update(long id, UserUpdateDTO dto);
}
