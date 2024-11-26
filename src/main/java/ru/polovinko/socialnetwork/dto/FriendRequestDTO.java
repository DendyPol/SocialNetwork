package ru.polovinko.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polovinko.socialnetwork.model.FriendshipStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {
  private Long id;
  private Long senderId;
  private Long recipientId;
  private FriendshipStatus status;
}
