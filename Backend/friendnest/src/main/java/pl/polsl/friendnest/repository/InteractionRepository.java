package pl.polsl.friendnest.repository;

import io.micrometer.common.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Integer countAllByPostAndInteractionType(Post post, Integer interactionId);

    boolean existsAllByUserAndPostAndAndInteractionType(User user, Post post, Integer interactionId);

}