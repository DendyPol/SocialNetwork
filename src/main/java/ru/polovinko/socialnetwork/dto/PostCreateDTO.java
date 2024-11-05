package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {
  @Valid
  private String content;
  @Valid
  private Long userId;
  @Valid
  private List<CommentDTO> comments;
  @Valid
  private List<LikeDTO> likes;
  @Valid
  private Long photoId;
}
