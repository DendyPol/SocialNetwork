package ru.polovinko.socialnetwork.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDTO {
  @Positive
  private long id;
  private String content;
  @Positive
  private long photoId;
}
