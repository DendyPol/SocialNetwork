package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
  @Valid
  private String password;
  @Valid
  private String username;
  @Valid
  private String email;
}
