package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.Chat;
import pl.polsl.friendnest.model.User;

public interface ChatService {

    Chat createChat(User user1, User user2);
}
