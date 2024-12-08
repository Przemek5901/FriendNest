package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Chat;
import pl.polsl.friendnest.model.ChatTo;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.repository.ChatRepository;
import pl.polsl.friendnest.repository.MessageRepository;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.ChatToService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatToServiceImpl implements ChatToService {

    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    public ChatToServiceImpl(UserRepository userRepository, ChatRepository chatRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<ChatTo> getChatList(Long userId) {
        if (userId == null) {
            throw new CustomException();
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(CustomException::new);

        List<ChatTo> chatToList = new ArrayList<>();
        List<Chat> chatList = chatRepository.findChatsByUser1OrUser2(user, user);

        for(Chat chat : chatList){
            ChatTo chatTo = new ChatTo();
            chatTo.setChat(chat);
            chatTo.setLastMessage(messageRepository.findLatestMessageByChat(chat));

            chatToList.add(chatTo);
        }

        return chatToList;
    }
}
