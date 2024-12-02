package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findCommentByCommentId(Long commentId);

    List<Comment> findCommentsByPost(Post post);

    Integer countByPost(Post post);
}