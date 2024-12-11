package ru.polovinko.socialnetwork.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeDTO {
  private Long id;
  private Long userId;
  private Long postId;
}
