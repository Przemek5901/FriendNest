package pl.polsl.friendnest.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.ChangeUserCredentialsRequest;
import pl.polsl.friendnest.model.response.SearchResults;
import pl.polsl.friendnest.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/changeUserCredentails")
    public ResponseEntity<User> changeUserCredentails(@RequestBody ChangeUserCredentialsRequest changeUserCredentialsRequest) {
        return ResponseEntity.ok(userService.changeUserCredentails(changeUserCredentialsRequest));

    }

}
