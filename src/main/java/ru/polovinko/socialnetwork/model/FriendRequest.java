package ru.polovinko.socialnetwork.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friend_requests")
public class FriendRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @ManyToOne
  @JoinColumn(name = "sender_id")
  private User sender;
  @ManyToOne
  @JoinColumn(name = "recipient_id")
  private User recipient;
  @Enumerated(EnumType.STRING)
  private FriendshipStatus requestStatus;
}
