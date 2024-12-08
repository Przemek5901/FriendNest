package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.Message;
import pl.polsl.friendnest.service.MessageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/getChatMessages")
    public ResponseEntity<List<Message>> getChatMessages(@RequestBody Map<String, Long> payload) {
        Long chatId = payload.get("chatId");
        return ResponseEntity.ok(messageService.getChatMessages(chatId));

    }

    @PostMapping("/sendMessage")
    public ResponseEntity<List<Message>> getChatToList(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.sendMessage(message));

    }
}
