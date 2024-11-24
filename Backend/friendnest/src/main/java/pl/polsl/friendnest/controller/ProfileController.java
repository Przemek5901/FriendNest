package pl.polsl.friendnest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.friendnest.model.Profile;
import pl.polsl.friendnest.model.request.ProfileRequest;
import pl.polsl.friendnest.service.ProfileService;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/getProfile")
    public ResponseEntity<Profile> register(@RequestBody Map<String, String> payload) {
        String login = payload.get("login");
        return ResponseEntity.ok(profileService.getProfile(login));

    }

    @PostMapping("/editProfile")
    public ResponseEntity<Profile> register(@RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.ok(profileService.editProfile(profileRequest));

    }
}
