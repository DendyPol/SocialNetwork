package ru.polovinko.socialnetwork.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.polovinko.socialnetwork.dto.UserSearchDTO;
import ru.polovinko.socialnetwork.model.User;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {
  private final UserSearchDTO dto;

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var predicate = builder.conjunction();
    if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("username"), "%" + dto.getUsername() + "%"));
    }
    if (dto.getFriendId() != null) {
      Join<User, User> friendsJoin = root.join("friends", JoinType.LEFT);
      predicate = builder.and(predicate, builder.equal(friendsJoin.get("id"), dto.getFriendId()));
    }
    query.groupBy(root.get("id"));
    return predicate;
  }
}
