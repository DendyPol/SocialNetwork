package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.UserCreateDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/search")
  public Page<UserDTO> search(@RequestBody UserSearchDTO dto, Pageable pageable) {
    return userService.search(dto, pageable);
  }

  @PostMapping
  public UserDTO createUser(@RequestBody UserCreateDTO dto) {
    return userService.create(dto);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable long id) {
    userService.delete(id);
  }

  @PutMapping
  public UserDTO update(@RequestBody UserUpdateDTO dto) {
    return userService.update(dto);
  }
}
