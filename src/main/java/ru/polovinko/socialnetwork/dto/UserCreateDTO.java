package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UserCreateDTO {
  @NotEmpty
  @NotNull
  private String password;
  @NotEmpty
  @NotNull
  private String username;
  @NotEmpty
  @NotNull
  private String email;
}
