package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Follow;
import pl.polsl.friendnest.model.FollowId;
import pl.polsl.friendnest.model.User;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    Long countAllByFollowed(User user);

    Long countAllByFollower(User user);
}