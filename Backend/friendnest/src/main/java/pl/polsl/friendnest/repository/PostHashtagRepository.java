package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.PostHashtag;
import pl.polsl.friendnest.model.PostHashtagId;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, PostHashtagId> {
}