package ru.polovinko.socialnetwork.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoDTO {
  private Long id;
  private String url;
  private Long userId;
}
