package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {
}