package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.request.AddInteractionRequest;
import pl.polsl.friendnest.model.response.UserInteractions;
import pl.polsl.friendnest.service.InteractionService;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    @PostMapping("/addInteraction")
    public ResponseEntity<UserInteractions> addInteraction(@RequestBody AddInteractionRequest interactionRequest) {
        return ResponseEntity.ok(interactionService.addInteraction(interactionRequest));

    }
}
