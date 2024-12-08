package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.polsl.friendnest.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    Optional<User> findByUserId(Long userId);

    @Query("SELECT u FROM User u WHERE (LOWER(u.login) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.profileName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:userId IS NULL OR u.userId != :userId)")
    List<User> searchUsersByKeyword(
            @Param("keyword") String keyword,
            @Param("userId") Long userId
    );


}