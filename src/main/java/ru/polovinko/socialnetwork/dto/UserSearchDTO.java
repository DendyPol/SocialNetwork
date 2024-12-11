package ru.polovinko.socialnetwork.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.polovinko.socialnetwork.model.FriendshipStatus;

import java.util.List;

@Data
@Builder
@Valid
public class UserSearchDTO {
  @Positive
  private Long userId;
  private List<@Positive Long> userIds;
  @NotEmpty
  private String username;
  @Enumerated(EnumType.STRING)
  private FriendshipStatus requestStatus;
}
