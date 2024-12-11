package ru.polovinko.socialnetwork.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
  private Long id;
  private String content;
  private Long userId;
  private Long postId;
}
