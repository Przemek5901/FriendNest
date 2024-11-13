package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}