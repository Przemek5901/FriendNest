package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Long countAllByUser(User user);

    List<Post> getPostsByUser(User user);
}