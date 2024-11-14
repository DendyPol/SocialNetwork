package ru.polovinko.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;

import java.util.Optional;

public interface UserService {
  Page<UserDTO> search(UserSearchDTO dto, Pageable pageable);

  Optional<UserDTO> findById(long id);

  UserDTO create(UserCreateDTO dto);

  void delete(long id);

  UserDTO update(UserUpdateDTO dto);
}
