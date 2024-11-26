package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class UserUpdateDTO {
  @Positive
  private long id;
  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String email;
}
