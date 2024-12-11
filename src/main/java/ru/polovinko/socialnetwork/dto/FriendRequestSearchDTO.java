package ru.polovinko.socialnetwork.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.polovinko.socialnetwork.model.FriendshipStatus;

@Data
@Builder
@Valid
public class FriendRequestSearchDTO {
  @Positive
  private Long senderId;
  @Positive
  private Long recipientId;
  @Enumerated(EnumType.STRING)
  private FriendshipStatus requestStatus;
}
