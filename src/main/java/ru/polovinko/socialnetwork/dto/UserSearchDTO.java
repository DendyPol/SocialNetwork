package ru.polovinko.socialnetwork.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
public class UserSearchDTO {
  @Positive
  private Long userId;
  @NotEmpty
  private String username;
  @Enumerated(EnumType.STRING)
  private FriendshipStatus status;
}
