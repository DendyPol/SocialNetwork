package ru.polovinko.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.socialnetwork.dto.LoginRequestDTO;
import ru.polovinko.socialnetwork.dto.UserDTO;
import ru.polovinko.socialnetwork.dto.UserUpdateDTO;
import ru.polovinko.socialnetwork.exception.ObjectNotFoundException;
import ru.polovinko.socialnetwork.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public List<UserDTO> findAll() {
    return userService.findAll();
  }

  @GetMapping("{id}")
  public UserDTO findById(@PathVariable long id) {
    return userService.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(String.format("User with ID %d not found", id)));
  }

  @PostMapping
  public UserDTO createUser(@RequestBody UserDTO dto) {
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

  @GetMapping("/{userId}/friends")
  public List<UserDTO> getUserFriends(@PathVariable long userId) {
    return userService.getUserFriend(userId);
  }

  @PutMapping("{id}")
  public UserDTO update(@PathVariable long id, @RequestBody UserUpdateDTO dto) {
    return userService.update(id, dto);
  }
}
