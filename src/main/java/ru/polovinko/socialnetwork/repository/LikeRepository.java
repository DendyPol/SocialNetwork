package ru.polovinko.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
  boolean existsByPostAndUser(Post post, User user);

  Optional<Like> findByPostAndUser(Post post, User user);

  int countByPost(Post post);
}
