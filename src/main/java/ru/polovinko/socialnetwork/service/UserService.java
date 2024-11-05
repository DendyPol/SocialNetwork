package ru.polovinko.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.model.User;

import java.util.Optional;

public interface UserService {
  Page<UserDTO> search(UserSearchDTO dto, Pageable pageable);

  Optional<UserDTO> findById(long id);

  UserDTO create(UserCreateDTO dto);

  void deleteById(long id);

  Optional<UserDTO> login(String username, String rawPassword);

  UserDTO update(UserUpdateDTO dto);

  void addFriend(User user, User friend);
}
