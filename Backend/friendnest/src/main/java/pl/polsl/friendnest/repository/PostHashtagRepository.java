package pl.polsl.friendnest.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.polsl.friendnest.model.Hashtag;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.PostHashtag;
import pl.polsl.friendnest.model.PostHashtagId;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, PostHashtagId> {

    List<PostHashtag> findByPost(Post post);

    @Query("SELECT ph.hashtag FROM PostHashtag ph GROUP BY ph.hashtag ORDER BY COUNT(ph.post) DESC")
    List<Hashtag> findTopHashtagsByPostCount(PageRequest pageable);

    Integer countByHashtag(Hashtag hashtag);

}