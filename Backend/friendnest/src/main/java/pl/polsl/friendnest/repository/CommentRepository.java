package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findCommentByCommentId(Long commentId);
}