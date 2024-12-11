package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class UserCreateDTO {
  @NotBlank
  @Size(min = 6, max = 30)
  private String password;
  @NotBlank
  @Size(min = 6, max = 30)
  private String username;
  @Email
  @NotBlank
  private String email;
}
