package ru.polovinko.socialnetwork.dto;

import lombok.Builder;
import lombok.Data;
import ru.polovinko.socialnetwork.model.FriendshipStatus;

@Data
@Builder
public class FriendRequestDTO {
  private Long id;
  private Long senderId;
  private Long recipientId;
  private FriendshipStatus requestStatus;
}
