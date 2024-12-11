package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class PhotoSearchDTO {
  @Positive
  private Long userId;
  @Positive
  private Long photoId;
  @NotEmpty
  private String url;
}
