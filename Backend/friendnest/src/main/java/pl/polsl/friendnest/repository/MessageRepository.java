package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.friendnest.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}