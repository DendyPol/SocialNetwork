package ru.polovinko.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.polovinko.socialnetwork.model.FriendRequest;
import ru.polovinko.socialnetwork.model.User;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long>, JpaSpecificationExecutor<FriendRequest> {
}
