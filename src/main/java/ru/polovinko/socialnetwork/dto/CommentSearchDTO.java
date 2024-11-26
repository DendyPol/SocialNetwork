package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class CommentSearchDTO {
  @NotBlank
  private String content;
  @Positive
  private Long postId;
  @NotBlank
  private String username;
}
