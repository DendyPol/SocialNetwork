package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class LikeCreateDTO {
  @Positive
  private long userId;
  @Positive
  private long postId;
}
