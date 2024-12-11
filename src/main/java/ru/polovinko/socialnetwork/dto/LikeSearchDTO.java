package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class LikeSearchDTO {
  @Positive
  private Long userId;
  @Positive
  private Long postId;
}
