package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.BlockedUser;
import pl.polsl.friendnest.model.BlockedUserId;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, BlockedUserId> {
}