package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Hashtag;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {

    Optional<Hashtag> findByTag(String tag);
}