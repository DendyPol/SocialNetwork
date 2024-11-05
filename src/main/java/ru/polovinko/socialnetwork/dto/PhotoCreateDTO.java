package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCreateDTO {
  @Valid
  private String url;
  @Valid
  private Long userId;
}
