package pl.polsl.friendnest.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.response.SearchResults;
import pl.polsl.friendnest.service.SearchService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<SearchResults> search(@RequestBody Map<String, String> payload) {
        String keyword = payload.get("keyword");
        String userId = payload.get("userId");
        return ResponseEntity.ok(searchService.searchAll(keyword, Long.valueOf(userId)));

    }

    @PostMapping("/searchUsers")
    public ResponseEntity<List<User>> searchUsers(@RequestBody Map<String, String> payload) {
        String keyword = payload.get("keyword");
        String userId = payload.get("userId");
        return ResponseEntity.ok(searchService.searchUsers(keyword, Long.valueOf(userId)));

    }

}
