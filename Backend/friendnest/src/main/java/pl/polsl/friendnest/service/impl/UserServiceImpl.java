package pl.polsl.friendnest.service.impl;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.model.User;
import pl.polsl.friendnest.model.request.ChangeUserCredentialsRequest;
import pl.polsl.friendnest.repository.UserRepository;
import pl.polsl.friendnest.service.UserService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            throw new CustomException();
        }

        return userRepository.findByUserId(userId).orElseThrow(CustomException::new);
    }

    @Override
    public User changeUserCredentails(ChangeUserCredentialsRequest changeUserCredentialsRequest) {
        if (!StringUtils.hasLength(changeUserCredentialsRequest.getLogin())) {
            throw new CustomException("Nie udało się zapisać zmian");
        }

        User user = userRepository.findByLogin(changeUserCredentialsRequest.getLogin())
                .orElseThrow(() -> new CustomException("Użytkownik nie został znaleziony"));

        validatePassword(changeUserCredentialsRequest.getOldPassword(), user.getPassword(),
                changeUserCredentialsRequest.getNewPassword(), changeUserCredentialsRequest.getRepeatNewPassword());

        boolean isUpdated = updateUserFields(changeUserCredentialsRequest, user);

        if (isUpdated) {
            user.setUpdatedAt(OffsetDateTime.now());
        }

        return userRepository.save(user);
    }

    private void validatePassword(String oldPassword, String storedPassword, String newPassword, String repeatNewPassword) {
        if (StringUtils.hasLength(oldPassword) || StringUtils.hasLength(newPassword) ||
                StringUtils.hasLength(repeatNewPassword)) {
            if (!StringUtils.hasLength(oldPassword)) {
                throw new CustomException("Podane stare hasło puste!");
            }

            if(!Objects.equals(newPassword, repeatNewPassword)) {
                throw new CustomException("Podane nowe hasła się nie zgadzają");
            }

            if (!passwordEncoder.matches(oldPassword, storedPassword)) {
                throw new CustomException("Podane stare hasło jest niepoprawne!");
            }
        }
    }

    private boolean updateUserFields(ChangeUserCredentialsRequest request, User user) {
        boolean isUpdated = false;

        if (StringUtils.hasLength(request.getLogin())) {
            user.setLogin(request.getLogin());
            isUpdated = true;
        }

        if (StringUtils.hasLength(request.getNewPassword())) {
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
            isUpdated = true;
        }

        if (StringUtils.hasLength(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
            isUpdated = true;
        }

        if (request.getBirthday() != null) {
            OffsetDateTime utcBirthday = request.getBirthday();
            LocalDate localDateBirthday = utcBirthday.atZoneSameInstant(ZoneOffset.systemDefault()).toLocalDate();
            user.setBirthdate(localDateBirthday);
            isUpdated = true;
        }


        return isUpdated;
    }
}
