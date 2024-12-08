package pl.polsl.friendnest.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.ChatTo;
import pl.polsl.friendnest.service.ChatToService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ChatToController {

    private final ChatToService chatToService;

    @PostMapping("/getChats")
    public ResponseEntity<List<ChatTo>> getChatToList(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        return ResponseEntity.ok(chatToService.getChatList(userId));

    }
}
