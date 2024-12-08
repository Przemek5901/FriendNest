package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Chat;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.repository.ChatRepository;
import pl.polsl.friendnest.service.ChatService;

import java.time.OffsetDateTime;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat createChat(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new CustomException("Nie udało się wysłać wiadomości");
        }

        var chat = Chat.builder().user1(user1).user2(user2).createdAt(OffsetDateTime.now()).build();

        chatRepository.save(chat);

        return chat;
    }
}
