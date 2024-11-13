package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}