package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class PostCreateDTO {
  @NotEmpty
  private String content;
  @Positive
  private long userId;
  @Positive
  private long photoId;
}
