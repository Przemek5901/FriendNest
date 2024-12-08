package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.polsl.friendnest.model.Chat;
import pl.polsl.friendnest.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.createdAt DESC LIMIT 1")
    Message findLatestMessageByChat(@Param("chat") Chat chat);

    List<Message> findMessagesByChat(Chat chat);

}