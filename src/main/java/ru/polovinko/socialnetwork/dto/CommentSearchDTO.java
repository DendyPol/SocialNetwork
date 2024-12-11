package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class CommentSearchDTO {
  @NotEmpty
  private String content;
  @Positive
  private Long postId;
  @NotEmpty
  private String username;
}
