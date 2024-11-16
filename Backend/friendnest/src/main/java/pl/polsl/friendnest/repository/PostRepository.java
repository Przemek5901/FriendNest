package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}