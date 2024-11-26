package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
  @NotBlank
  private String password;
  @NotBlank
  private String username;
  @NotBlank
  private String email;
}
