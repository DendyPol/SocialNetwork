package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class PhotoCreateDTO {
  @NotBlank
  private String url;
  @Positive
  private long userId;
}
