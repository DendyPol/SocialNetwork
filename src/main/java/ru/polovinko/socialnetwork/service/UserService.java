package ru.polovinko.socialnetwork.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;

public interface UserService {
  Page<UserDTO> search(@Valid UserSearchDTO dto, Pageable pageable);

  UserDTO create(@Valid UserCreateDTO dto);

  void delete(long id);

  UserDTO update(@Valid UserUpdateDTO dto);
}
