package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.model.FriendRequest;
import ru.polovinko.socialnetwork.model.User;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {
  private final UserSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var predicate = builder.conjunction();
    if (dto.getUserId() != null) {
      predicate = builder.and(predicate, builder.equal(root.get("id"), dto.getUserId()));
    }
    if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
      predicate = builder.and(predicate, root.get("id").in(dto.getUserIds()));
    }
    if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("username"), "%" + dto.getUsername() + "%"));
    }
    if (dto.getRequestStatus() != null) {
      Join<User, FriendRequest> sentRequestJoin = root.join("sentRequest", JoinType.LEFT);
      Join<User, FriendRequest> recipientRequestJoin = root.join("recipientRequest", JoinType.LEFT);
      var sent = builder.equal(sentRequestJoin.get("status"), dto.getRequestStatus());
      var recipient = builder.equal(recipientRequestJoin.get("status"), dto.getRequestStatus());
      predicate = builder.and(predicate, builder.or(sent, recipient));
    }
    query.groupBy(root.get("id"));
    return predicate;
  }
}
