package ru.polovinko.socialnetwork.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
  private Long id;
  private String password;
  private String username;
  private String email;
}
