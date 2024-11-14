package ru.polovinko.socialnetwork.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String url;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
