package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.LikeSearchDTO;
import ru.polovinko.socialnetwork.model.Like;

@RequiredArgsConstructor
public class LikeSpecification implements Specification<Like> {
  private final LikeSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<Like> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var predicate = builder.conjunction();
    if (dto.getUserId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("user").get("id"), dto.getUserId()));
    }
    if (dto.getPostId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("post").get("id"), dto.getPostId()));
    }
    return predicate;
  }
}
