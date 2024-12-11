package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class UserUpdateDTO {
  @Positive
  public long id;
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
  @NotEmpty
  @Email
  private String email;
}
