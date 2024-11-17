package pl.polsl.friendnest.auth.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.polsl.friendnest.auth.dto.AuthenticationResponse;
import pl.polsl.friendnest.auth.dto.RegisterRequest;
import pl.polsl.friendnest.auth.dto.AuthenticationRequest;
import pl.polsl.friendnest.config.JwtService;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.repository.UserRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthentiacionService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new CustomException("Użytkownik o takim loginie już istnieje");
        }

        String defaultProfileImage = "default/profile.png";
        String defaultBackgroundImage = "default/background.png";

        var user = User.builder()
                .userName(request.getUserName())
                .login(request.getLogin())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .profileName(request.getUserName())
                .profileImageUrl(defaultProfileImage)
                .backgroundImageUrl("zdj back")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).user(user).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new CustomException("Nieprawidłowy login lub hasło");
        }

        var user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new CustomException("Nieprawidłowy login lub hasło"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).user(user).build();
    }
}
