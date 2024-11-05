package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeCreateDTO {
  @Valid
  private Long userId;
  @Valid
  private Long postId;
}
