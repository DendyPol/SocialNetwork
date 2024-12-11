package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class CommentCreateDTO {
  @Size(max = 300)
  @NotBlank
  private String content;
  @Positive
  private long postId;
  @Positive
  private long userId;
}
