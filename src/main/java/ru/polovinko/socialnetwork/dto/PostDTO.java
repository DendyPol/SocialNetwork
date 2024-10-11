package ru.polovinko.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
  private Long id;
  private String content;
  private Long userId;
  private List<CommentDTO> comments;
  private List<LikeDTO> likes;
  private Long photoId;
}
