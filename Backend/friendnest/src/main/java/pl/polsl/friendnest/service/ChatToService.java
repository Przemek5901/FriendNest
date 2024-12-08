package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.ChatTo;

import java.util.List;

public interface ChatToService {

    List<ChatTo> getChatList(Long userId);
}
