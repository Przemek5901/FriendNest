package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.friendnest.model.Profile;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.ProfileRequest;
import pl.polsl.friendnest.service.ProfileService;
import pl.polsl.friendnest.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @PostMapping("/getProfile")
    public ResponseEntity<Profile> getProfile(@RequestBody Map<String, String> payload) {
        String login = payload.get("login");
        return ResponseEntity.ok(profileService.getProfile(login));

    }

    @PostMapping("/editProfile")
    public ResponseEntity<Profile> editProfile(@RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.ok(profileService.editProfile(profileRequest));

    }

    @PostMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        return ResponseEntity.ok(userService.getUserById(userId));

    }
}
