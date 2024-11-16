package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
}