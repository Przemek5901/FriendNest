package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.HashtagTo;
import pl.polsl.friendnest.service.HashtagService;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/getHashtags")
    public ResponseEntity<List<HashtagTo>> getHashtags() {
        return ResponseEntity.ok(hashtagService.getHashtagToList());
    }

}
