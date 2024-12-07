package pl.polsl.friendnest.service;

import pl.polsl.friendnest.model.response.SearchResults;

public interface SearchService {

    SearchResults searchAll(String keyword, Long userId);
}
