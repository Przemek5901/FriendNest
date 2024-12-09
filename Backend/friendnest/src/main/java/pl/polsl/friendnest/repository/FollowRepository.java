package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Follow;
import pl.polsl.friendnest.model.FollowId;
import pl.polsl.friendnest.model.User;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    Long countAllByFollowed(User user);

    Long countAllByFollower(User user);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    List<Follow> findFollowsByFollowed(User user);

    List<Follow> findFollowsByFollower(User user);
}