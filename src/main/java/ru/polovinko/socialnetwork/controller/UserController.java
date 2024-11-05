package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.*;
import ru.polovinko.socialnetwork.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/search")
  public Page<UserDTO> search(@RequestBody UserSearchDTO dto, Pageable pageable) {
    return userService.search(dto, pageable);
  }

  @GetMapping("{id}")
  public Optional<UserDTO> findById(@PathVariable long id) {
    return userService.findById(id);
  }

  @PostMapping
  public UserDTO createUser(@RequestBody UserCreateDTO dto) {
    return userService.create(dto);
  }

  @PostMapping("/login")
  public UserDTO login(@RequestBody LoginRequestDTO dto) {
    return userService.login(dto.getUsername(), dto.getPassword())
      .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
  }

  @DeleteMapping("{id}")
  public void deleteUserById(@PathVariable long id) {
    userService.deleteById(id);
  }

  @PutMapping
  public UserDTO update(@RequestBody UserUpdateDTO dto) {
    return userService.update(dto);
  }
}
