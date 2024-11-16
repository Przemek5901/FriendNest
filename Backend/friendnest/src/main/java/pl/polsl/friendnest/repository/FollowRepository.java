package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Follow;
import pl.polsl.friendnest.model.FollowId;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}