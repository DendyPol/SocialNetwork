package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class PostUpdateDTO {
  @Positive
  private long id;
  @NotEmpty
  private String content;
  @Positive
  private long photoId;
}
