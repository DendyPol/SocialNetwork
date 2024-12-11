package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.FriendRequestSearchDTO;
import ru.polovinko.socialnetwork.model.FriendRequest;

@RequiredArgsConstructor
public class FriendRequestSpecification implements Specification<FriendRequest> {
  private final FriendRequestSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<FriendRequest> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var res = builder.conjunction();
    if (dto.getSenderId() != null) {
      res = builder.and(res, builder.equal(root.get("sender").get("id"), dto.getSenderId()));
    }
    if (dto.getRecipientId() != null) {
      res = builder.and(res, builder.equal(root.get("recipient").get("id"), dto.getRecipientId()));
    }
    if (dto.getRequestStatus() != null) {
      res = builder.and(res, builder.equal(root.get("status"), dto.getRequestStatus()));
    }
    return res;
  }
}
