package pl.polsl.friendnest.repository;

import io.micrometer.common.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.polsl.friendnest.model.Comment;
import pl.polsl.friendnest.model.Interaction;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

    List<Interaction> findByUser(User user);

    Optional<Interaction> findByUserAndPostAndInteractionTypeAndComment(
            User user, Post post, Integer interactionType, @Nullable Comment comment);

    Integer countAllByPostAndInteractionTypeAndComment(Post post, Integer interactionId, Comment comment);
    boolean existsAllByUserAndPostAndAndInteractionTypeAndComment(User user, Post post, Integer interactionId, Comment comment);

    List<Interaction> getInteractionsByInteractionType(Integer interactionType);


}