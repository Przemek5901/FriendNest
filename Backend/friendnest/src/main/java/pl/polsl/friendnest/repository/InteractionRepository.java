package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Interaction;
import pl.polsl.friendnest.model.User;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

    List<Interaction> findByUser(User user);
}