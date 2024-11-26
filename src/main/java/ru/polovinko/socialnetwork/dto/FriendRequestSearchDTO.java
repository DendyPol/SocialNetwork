package ru.polovinko.socialnetwork.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polovinko.socialnetwork.model.FriendshipStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class FriendRequestSearchDTO {
  @Positive
  private Long senderId;
  @Positive
  private Long recipientId;
  private FriendshipStatus status;
}
