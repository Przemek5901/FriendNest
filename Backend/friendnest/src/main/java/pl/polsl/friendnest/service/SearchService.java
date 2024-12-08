package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.response.SearchResults;

import java.util.List;

public interface SearchService {

    SearchResults searchAll(String keyword, Long userId);

    List<User> searchUsers(String keyword, Long userId);
}
