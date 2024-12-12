package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.polsl.friendnest.model.Interaction;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    Long countAllByUser(User user);

    List<Post> getPostsByUser(User user);

    List<Post> findByUserNot(User user);

    Optional<Post> findPostByPostId(Long postId);

    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> searchPosts(@Param("keyword") String keyword);

    @Query("select i from Interaction i " +
            "where i.interactionType = :interactionType " +
            "and (:includeUser = true and i.user = :user or :includeUser = false and i.user != :user)")
    List<Interaction> getInteractionsByTypeAndUserCondition(
            @Param("interactionType") Long interactionType,
            @Param("user") User user,
            @Param("includeUser") boolean includeUser);

    Optional<Post> getPostsByPostId(Long postId);

}