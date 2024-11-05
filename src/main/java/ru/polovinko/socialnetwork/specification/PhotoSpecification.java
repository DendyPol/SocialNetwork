package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.PhotoSearchDTO;
import ru.polovinko.socialnetwork.model.Photo;

@RequiredArgsConstructor
public class PhotoSpecification implements Specification<Photo> {
  private final PhotoSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<Photo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var predicate = builder.conjunction();
    if (dto.getUserId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("user").get("id"), dto.getUserId()));
    }
    if (dto.getPhotoId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("id"), dto.getPhotoId()));
    }
    if (dto.getUrl() != null && !dto.getUrl().isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("url"), "%" + dto.getUrl() + "%"));
    }
    return predicate;
  }
}
