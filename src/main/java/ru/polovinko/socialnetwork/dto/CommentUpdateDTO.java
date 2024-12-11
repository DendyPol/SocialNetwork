package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class CommentUpdateDTO {
  @Positive
  private long id;
  @Size(max = 300)
  @NotEmpty
  private String content;
}
