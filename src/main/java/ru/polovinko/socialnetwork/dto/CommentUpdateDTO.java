package ru.polovinko.socialnetwork.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDTO {
  @Positive
  private long id;
  @Size(min = 1, max = 300)
  private String content;
}
