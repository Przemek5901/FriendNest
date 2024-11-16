package pl.polsl.friendnest.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.friendnest.auth.service.AuthentiacionService;
import pl.polsl.friendnest.auth.dto.AuthenticationRequest;
import pl.polsl.friendnest.auth.dto.AuthenticationResponse;
import pl.polsl.friendnest.auth.dto.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthentiacionService authentiacionService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authentiacionService.register(request));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authentiacionService.authenticate(request));
    }

}
