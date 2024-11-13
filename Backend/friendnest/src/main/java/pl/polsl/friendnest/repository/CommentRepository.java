package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}