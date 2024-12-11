package ru.polovinko.socialnetwork.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDTO {
  private Long id;
  private String content;
  private Long userId;
  private List<CommentDTO> comments;
  private List<LikeDTO> likes;
  private Long photoId;
}
