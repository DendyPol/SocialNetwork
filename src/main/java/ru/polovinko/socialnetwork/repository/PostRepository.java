package ru.polovinko.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.socialnetwork.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
