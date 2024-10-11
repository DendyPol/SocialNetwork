package ru.polovinko.socialnetwork.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
  private Long id;
  @Size(max = 300, message = "Comment cannot be longer than 300 characters")
  private String content;
  private Long userId;
  private Long postId;
}
