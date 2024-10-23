package ru.polovinko.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.polovinko.socialnetwork.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
}
