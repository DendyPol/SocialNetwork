package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.CommentSearchDTO;
import ru.polovinko.socialnetwork.model.Comment;
import ru.polovinko.socialnetwork.model.Post;

@RequiredArgsConstructor
public class CommentSpecification implements Specification<Comment> {
  private final CommentSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var res = builder.conjunction();
    Join<Comment, Post> post = root.join("post", JoinType.LEFT);
    if (dto.getContent() != null && !dto.getContent().isEmpty()) {
      res = builder.and(res, builder.like(root.get("content"), "%" + dto.getContent() + "%"));
    }
    if (dto.getPostId() != null) {
      res = builder.and(res, builder.equal(post.get("id"), dto.getPostId()));
    }
    if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
      res = builder.and(res, builder.equal(root.get("user").get("username"), dto.getUsername()));
    }
    query.groupBy(root.get("id"));
    return res;
  }
}
