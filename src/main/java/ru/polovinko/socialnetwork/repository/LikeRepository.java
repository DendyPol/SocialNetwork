package ru.polovinko.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.polovinko.socialnetwork.model.Like;
import ru.polovinko.socialnetwork.model.Post;
import ru.polovinko.socialnetwork.model.User;

public interface LikeRepository extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {
  boolean existsByPostAndUser(Post post, User user);
}
