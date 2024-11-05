package ru.polovinko.socialnetwork.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestCreateDTO {
  @NotNull
  private Long userId;
  @NotNull
  private Long friendId;
}
