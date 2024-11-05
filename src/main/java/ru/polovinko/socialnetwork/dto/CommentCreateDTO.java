package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDTO {
  @Valid
  @Size(max = 300, message = "Comment cannot be longer than 300 characters")
  private String content;
  @Valid
  private Long postId;
  @Valid
  private Long userId;
}
