package ru.polovinko.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestSearchDTO {
  private Long userId;
  private Long friendId;
  private Boolean accepted;
}
