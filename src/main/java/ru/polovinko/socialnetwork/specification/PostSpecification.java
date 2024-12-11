package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.PostSearchDTO;
import ru.polovinko.socialnetwork.model.Post;

@RequiredArgsConstructor
public class PostSpecification implements Specification<Post> {
  private final PostSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var predicate = builder.conjunction();
    if (dto.getPostId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("id"), dto.getPostId()));
    }
    if (dto.getUserId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("user").get("id"), dto.getUserId()));
    }
    if (dto.getContent() != null && !dto.getContent().isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("content"), "%" + dto.getContent() + "%"));
    }
    if (dto.getPhotoId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("photo").get("id"), dto.getPhotoId()));
    }
    return predicate;
  }
}
