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
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class FriendRequestUpdateDTO {
  @Positive
  private long id;
  private FriendshipStatus status;
}
