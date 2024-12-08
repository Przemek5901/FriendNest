package pl.polsl.friendnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.polsl.friendnest.model.Chat;
import pl.polsl.friendnest.model.Post;
import pl.polsl.friendnest.model.User;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    List<Chat> findChatsByUser1OrUser2(User user1,User user2);
    Optional<Chat> findChatByChatId(Long chatId);
}