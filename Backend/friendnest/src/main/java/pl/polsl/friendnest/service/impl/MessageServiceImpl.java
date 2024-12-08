package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.Chat;
import pl.polsl.friendnest.model.Message;
import pl.polsl.friendnest.repository.ChatRepository;
import pl.polsl.friendnest.repository.MessageRepository;
import pl.polsl.friendnest.service.ChatService;
import pl.polsl.friendnest.service.FileService;
import pl.polsl.friendnest.service.MessageService;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    private final FileService fileService;

    private final ChatService chatService;

    public MessageServiceImpl(ChatRepository chatRepository, MessageRepository messageRepository, FileService fileService, ChatService chatService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.fileService = fileService;
        this.chatService = chatService;
    }

    @Override
    public List<Message> getChatMessages(Long chatId) {
        if (chatId == null) {
            throw new CustomException("Nie udało się wczytać czatu");
        }

        Chat chat = chatRepository.findChatByChatId(chatId).orElseThrow(CustomException::new);

        return messageRepository.findMessagesByChat(chat);
    }

    @Override
    public List<Message> sendMessage(Message message) {
        if(message == null) {
            throw new CustomException("Nie udało się wysłać wiadomości");
        }

        if(message.getChat().getChatId() == null) {
            message.setChat(chatService.createChat(message.getChat().getUser1(), message.getChat().getUser2()));
        }

        message.setCreatedAt(OffsetDateTime.now());

        if (!StringUtils.hasLength(message.getImageBase64()) && !StringUtils.hasLength(message.getContent())) {
            throw new CustomException("Nie udało się wysłać wiadomości");
        }

        if (StringUtils.hasLength(message.getImageBase64())) {
            message.setImageUrl(fileService.saveFileFromBase64(message.getImageBase64(), "messages"));
        }

        messageRepository.save(message);

        return messageRepository.findMessagesByChat(message.getChat());
    }
}
