package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Message;

import java.util.List;

public interface MessageService {

    List<Message> getChatMessages(Long chatId);

    List<Message> sendMessage(Message message);
}
